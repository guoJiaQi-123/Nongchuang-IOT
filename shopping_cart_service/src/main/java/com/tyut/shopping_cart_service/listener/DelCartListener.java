package com.tyut.shopping_cart_service.listener;

import com.tyut.shopping_common.service.CartService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @author OldGj 2024/6/22
 * @apiNote 删除购物车中数据rocket消息监听器
 */
@Service
@RocketMQMessageListener(topic = "del_cart_queue", consumerGroup = "del_cart_group")
public class DelCartListener implements RocketMQListener<Long> {

    @Autowired
    private CartService cartService;

    @Override
    public void onMessage(Long id) {
        cartService.deleteCartGoods(id);
    }
}
