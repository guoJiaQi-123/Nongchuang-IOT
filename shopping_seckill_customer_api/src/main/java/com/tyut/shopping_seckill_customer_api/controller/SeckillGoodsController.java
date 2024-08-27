package com.tyut.shopping_seckill_customer_api.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Orders;
import com.tyut.shopping_common.pojo.SeckillGoods;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.OrdersService;
import com.tyut.shopping_common.service.SeckillService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * @version v1.0
 * @author OldGj 2024/7/6
 * @apiNote 秒杀商品控制器
 */
@RestController
@RequestMapping("/user/seckillGoods")
public class SeckillGoodsController {

    @DubboReference
    private SeckillService seckillService;
    @DubboReference
    private OrdersService ordersService;

    /**
     * 用户分页查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/findPage")
    public BaseResult<Page<SeckillGoods>> getPage(int page, int size) {
        Page<SeckillGoods> seckillGoodsPage = seckillService.findPageByRedis(page, size);
        return BaseResult.ok(seckillGoodsPage);
    }

    /**
     * 用户查询秒杀商品详情
     * @param id 商品Id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseResult<SeckillGoods> findById(Long id) {
        // 先从Redis中查询秒杀商品详情
        SeckillGoods seckillGoods = seckillService.findSeckillGoodsByRedis(id);
        if (seckillGoods != null) {
            return BaseResult.ok(seckillGoods);
        } else { // 如果Redis中不存在秒杀商品，再从数据库中查询
            // 将从MySQL中查询秒杀商品详情的方法进行流量防护
            SeckillGoods seckillGoodsByMySql = seckillService.findSeckillGoodsByMySql(id);
            return BaseResult.ok(seckillGoodsByMySql);
        }
    }

    /**
     * 生成秒杀订单
     * @param orders 订单对象
     * @return 生成的订单
     */
    @PostMapping("/add")
    public BaseResult<Orders> add(@RequestBody Orders orders, @RequestHeader("userId") Long userId) {
        orders.setUserId(userId);
        Orders order = seckillService.createOrder(orders);
        return BaseResult.ok(order);
    }

    /**
     * 根据秒杀订单的ID查询秒杀订单
     * @param id 秒杀订单的ID
     * @return 秒杀订单
     */
    @GetMapping("/findOrder")
    public BaseResult<Orders> findOrder(String id) {
        Orders order = seckillService.findOrder(id);
        return BaseResult.ok(order);
    }


    @GetMapping("/pay")
    public BaseResult pay(String id) {
        // 支付订单，生成订单数据
        Orders orders = seckillService.pay(id);
        // 将订单数据插入到数据库
        ordersService.add(orders);
        return BaseResult.ok();
    }

}
