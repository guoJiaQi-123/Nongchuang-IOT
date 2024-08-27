package com.tyut.shopping_order_service.listener;

import com.tyut.shopping_common.pojo.Orders;
import com.tyut.shopping_common.service.OrdersService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @author OldGj 2024/7/5
 * @apiNote 监听消息过期时间
 */
@Service
@RocketMQMessageListener(topic = "check_orders_queue", consumerGroup = "check_orders_group")
public class CheckOrdersListener implements RocketMQListener<String> {

    @Autowired
    private OrdersService ordersService;


    @Override
    public void onMessage(String orderId) {
        // 查询订单
        Orders orders = ordersService.findById(orderId);
        // 如果此时的订单状态任然为未支付，则将订单状态改为交易关闭
        if (orders.getStatus() == 1) {
            orders.setStatus(6);
            ordersService.update(orders);
        }
    }
}
