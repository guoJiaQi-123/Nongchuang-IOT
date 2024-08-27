package com.tyut.shopping_cart_service.listener;

import com.tyut.shopping_common.enu.RocketmqTopicEnum;
import com.tyut.shopping_common.pojo.CartGoods;
import com.tyut.shopping_common.service.CartService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @author OldGj 2024/6/22
 * @apiNote 同步商品数据到购物车rocketmq监听器
 */
@Service
@RocketMQMessageListener(topic = "sync_cart_queue", consumerGroup = "sync_cart_group")
public class SyncCartListener implements RocketMQListener<CartGoods> {
    @Autowired
    private CartService cartService;


    @Override
    public void onMessage(CartGoods cartGoods) {
        cartService.refreshCartGoods(cartGoods);
    }
}
