package com.tyut.shopping_seckill_service.redis;

import com.tyut.shopping_common.pojo.CartGoods;
import com.tyut.shopping_common.pojo.Orders;
import com.tyut.shopping_common.pojo.SeckillGoods;
import com.tyut.shopping_common.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @version v1.0
 * @author OldGj 2024/7/6
 * @apiNote Redis过期监听器
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillService seckillService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 根据过期的键，获取订单副本
        String expiredKey = message.toString();
        Orders oldOrders = (Orders) redisTemplate.opsForValue().get(expiredKey + "_copy");
        // 根据订单副本，获取当前订单的秒杀商品数量
        CartGoods cartGoods = oldOrders.getCartGoods().get(0);
        Integer num = cartGoods.getNum(); // 商品数量
        Long goodId = cartGoods.getGoodId(); // 商品ID
        // 查询秒杀商品
        SeckillGoods seckillGoods = seckillService.findSeckillGoodsByRedis(goodId);
        // 修改商品库存
        seckillGoods.setStockCount(seckillGoods.getStockCount() + num);
        redisTemplate.boundHashOps("seckillGoods").put(goodId, seckillGoods);
        // 删除复制订单数据
        redisTemplate.delete(expiredKey + "_copy");
    }
}
