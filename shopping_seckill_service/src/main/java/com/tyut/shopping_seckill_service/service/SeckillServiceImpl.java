package com.tyut.shopping_seckill_service.service;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.CartGoods;
import com.tyut.shopping_common.pojo.Orders;
import com.tyut.shopping_common.pojo.SeckillGoods;
import com.tyut.shopping_common.result.BusException;
import com.tyut.shopping_common.result.CodeEnum;
import com.tyut.shopping_common.service.SeckillService;
import com.tyut.shopping_seckill_service.mapper.SeckillGoodsMapper;
import com.tyut.shopping_seckill_service.redis.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @version v1.0
 * @author OldGj 2024/7/6
 * @apiNote 秒杀商品业务层
 */
@DubboService
@Slf4j
public class SeckillServiceImpl implements SeckillService {


    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BitMapBloomFilter bitMapBloomFilter;
    @Autowired
    private RedissonLock redissonLock;

    /**
     * 每分钟将数据库中的秒杀商品同步到Redis
     * 同步条件为：秒杀开始时间 < 当前时间 < 秒杀结束时间  &&  秒杀商品的库存 > 0
     */
    @Scheduled(cron = "0 * * * * *") // 每分钟执行一次
    public void refreshRedis() {
        // 先将Redis中的秒杀商品库存同步到MySQL中
        List<SeckillGoods> seckillGoodsRedis = redisTemplate.boundHashOps("seckillGoods").values();
        for (SeckillGoods seckillGoods : seckillGoodsRedis) {
            // 在数据库中查询秒杀商品
            LambdaQueryWrapper<SeckillGoods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SeckillGoods::getGoodsId, seckillGoods.getGoodsId());
            SeckillGoods mysqlGoods = seckillGoodsMapper.selectOne(lambdaQueryWrapper);
            if (mysqlGoods != null) {
                // 修改数据库中秒杀商品的库存，和redis中的库存保持一致
                mysqlGoods.setStockCount(seckillGoods.getStockCount());
                seckillGoodsMapper.updateById(mysqlGoods);
            }
        }


        // 1. 将MySQL中符合条件的秒杀商品查询出来
        log.info("将MySQL中的秒杀商品同步到Redis中");
        LambdaQueryWrapper<SeckillGoods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Date date = new Date();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        lambdaQueryWrapper.le(SeckillGoods::getStartTime, now)// 秒杀开始时间 < 当前时间 < 秒杀结束时间
                .ge(SeckillGoods::getEndTime, now)
                .gt(SeckillGoods::getStockCount, 0); //   秒杀商品的库存 > 0
        List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectList(lambdaQueryWrapper);
        // 2.  将原来Redis中保存的秒杀商品删除
        redisTemplate.delete("seckillGoods");
        // 3. 将最新查询出来的满足条件的秒杀商品同步到Redis和布隆过滤器中
        for (SeckillGoods seckillGood : seckillGoods) {
            redisTemplate.boundHashOps("seckillGoods").put(seckillGood.getGoodsId(), seckillGood);
            bitMapBloomFilter.add(seckillGood.getGoodsId().toString());
        }
    }


    /**
     * 前台用户查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    @Override
    public Page<SeckillGoods> findPageByRedis(int page, int size) {
        // 1. 从Redis中查询出所有秒杀商品
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
        // 2. 计算分页条件
        int start = (page - 1) * size;
        int end = Math.min(start + size, seckillGoodsList.size());
        // 截取当前页的记录
        List<SeckillGoods> currSeckillGoodsList = seckillGoodsList.subList(start, end);
        Page<SeckillGoods> seckillPage = new Page<>();
        seckillPage.setRecords(currSeckillGoodsList) // 当前页的记录
                .setTotal(seckillGoodsList.size()) // 总条数
                .setSize(size) // 每页条数
                .setCurrent(page); // 当前页
        return seckillPage;
    }

    /**
     * 将一个秒杀商品保存到redis中
     * @param seckillGoods 秒杀商品对象
     */
    @Override
    public void addRedisSeckillGoods(SeckillGoods seckillGoods) {
        redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getGoodsId(), seckillGoods);
        bitMapBloomFilter.add(seckillGoods.getGoodsId().toString());
    }

    /**
     * 查询秒杀商品详情
     * @param goodsId 秒杀商品对应的商品Id
     * @return 查询结果
     */
    @Override
    public SeckillGoods findSeckillGoodsByRedis(Long goodsId) {
        // 先用布隆过滤器判断秒杀商品是否存在，如果不存在，直接返回null
        if (!bitMapBloomFilter.contains(goodsId.toString())) {
            log.error("布隆过滤器判断商品不存在");
            return null;
        }
        // 1.从redis中查询秒杀商品
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(goodsId);
        // 2.如果查到商品，返回
        if (seckillGoods != null) {
            System.out.println("从redis中查询秒杀商品");
            return seckillGoods;
        } else {
            return null;
        }
    }


    /**
     * 生成秒杀订单
     * @param orders 订单数据
     * @return
     */
    @Override
    public Orders createOrder(Orders orders) {
        String lockKey = orders.getCartGoods().get(0).getGoodId().toString();
        // 加分布式锁，如果获取锁成功，执行下面代码，否则线程阻塞
        if (redissonLock.lock(lockKey, 10000)) {
            try {
                // 1. 构建订单对象
                orders.setId(IdWorker.getIdStr()); // 设置订单ID
                orders.setCreateTime(new Date());
                orders.setStatus(1); // 订单状态：未付款
                orders.setExpire(new Date(new Date().getTime() + 1000 * 60)); // 过期时间 1min
                // 计算商品价格
                CartGoods cartGoods = orders.getCartGoods().get(0);
                BigDecimal price = cartGoods.getPrice();
                Integer num = cartGoods.getNum();
                BigDecimal sum = price.multiply(new BigDecimal(num));
                orders.setPayment(sum);
                // 2.减少秒杀商品库存
                SeckillGoods seckillGoods = findSeckillGoodsByRedis(cartGoods.getGoodId());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 商品库存
                if (seckillGoods == null || seckillGoods.getStockCount() <= 0) {
                    throw new BusException(CodeEnum.NO_STOCK_ERROR);
                }

                seckillGoods.setStockCount(seckillGoods.getStockCount() - num);
                redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getGoodsId(), seckillGoods);

                // 3.保存订单数据
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                // 设置订单过期时间
                redisTemplate.opsForValue().set(orders.getId(), orders, 30, TimeUnit.MINUTES);
                /*
                    将订单保存一份副本，副本的过期时间略长于原订单
                    redis过期后触发过期事件时，redis数据已经过期，此时只能拿到key，拿不到value。
                    而过期事件需要回退商品库存，必须拿到value即订单详情，才能拿到商品数据，进行回退操作
                    我们保存一个订单副本，过期时间长于原订单，此时就可以通过副本拿到原订单数据
                 */
                redisTemplate.opsForValue().set(orders.getId() + "_copy", orders, 31, TimeUnit.MINUTES);
                System.out.println("下单成功,订单号:" + orders.getId() + ",当前库存：" + seckillGoods.getStockCount());
                return orders;
            } finally {
                redissonLock.unlock(lockKey);
            }
        } else {
            return null;
        }

    }

    /**
     * 根据id查询秒杀订单
     * @param id 订单id
     * @return
     */
    @Override
    public Orders findOrder(String id) {
        return (Orders) redisTemplate.opsForValue().get(id);
    }

    /**
     * 支付秒杀订单
     * @param orderId 订单id
     * @return
     */
    @Override
    public Orders pay(String orderId) {
        // 1. 从Redis中查询订单
        Orders order = findOrder(orderId);
        if (order == null) {
            throw new BusException(CodeEnum.ORDER_EXPIRED_ERROR); // 订单不存在或已经过期
        }
        // 2. 设置订单状态等信息
        order.setStatus(2); // 已支付
        order.setPaymentTime(new Date()); // 付款时间
        order.setPaymentType(2); // 支付宝支付
        // 3. 从Redis中删除订单数据
        redisTemplate.delete(orderId);
        redisTemplate.delete(orderId + "_copy");
        // 4.返回订单数据
        return order;
    }

    /**
     * 降级处理
     * @return 空值
     */
    public SeckillGoods mysqlBlockHandler(Long goodsId, BlockException e) {
        System.out.println("服务降级处理");
        return null;
    }

    /**
     * 从数据库中查询秒杀商品详情
     * @param goodsId
     * @return
     */
    @SentinelResource(value = "findSeckillGoodsByMySql", blockHandler = "mysqlBlockHandler")
    @Override
    public SeckillGoods findSeckillGoodsByMySql(Long goodsId) {
        // 3.如果没有查到商品，从数据库查询秒杀商品
        QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsId", goodsId);
        SeckillGoods seckillGoodsMysql = seckillGoodsMapper.selectOne(queryWrapper);
        System.out.println("从mysql中查询秒杀商品");
        // 4.如果该商品不在秒杀状态，抛出异常
        Date now = new Date();
        if (seckillGoodsMysql == null
                || now.after(seckillGoodsMysql.getEndTime())
                || now.before(seckillGoodsMysql.getStartTime())
                || seckillGoodsMysql.getStockCount() <= 0
        ) {
            return null;
        }
        // 5.如果该商品在秒杀状态，将商品保存到redis，并返回该商品
        addRedisSeckillGoods(seckillGoodsMysql);
        return seckillGoodsMysql;
    }


}
