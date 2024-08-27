package com.tyut.shopping_order_service.service;

import com.tyut.shopping_common.enu.RocketmqTopicEnum;
import com.tyut.shopping_common.pojo.CartGoods;
import com.tyut.shopping_common.pojo.Orders;
import com.tyut.shopping_common.service.OrdersService;
import com.tyut.shopping_order_service.mapper.CartGoodsMapper;
import com.tyut.shopping_order_service.mapper.OrdersMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/23
 * @apiNote 订单服务业务层实现类
 */
@DubboService
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private CartGoodsMapper cartGoodsMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 生成订单
     * @param orders
     * @return
     */
    @Override
    public Orders add(Orders orders) {
        // 只有订单状态没有设置的时候才设置为未付款
        // 在秒杀订单插入数据库时，是支付完成后才进行持久化，此时状态已经为已支付，不修改订单状态
        if (orders.getStatus() == null) {
            // 1. 订单状态为未付款
            orders.setStatus(1);
        }
        // 2. 设置订单创建时间
        orders.setCreateTime(new Date());
        // 3.设置订单金额
        List<CartGoods> cartGoods = orders.getCartGoods();
        BigDecimal sum = BigDecimal.ZERO;
        for (CartGoods cartGood : cartGoods) {
            // 商品数量
            Integer num = cartGood.getNum();
            // 商品单价
            BigDecimal price = cartGood.getPrice();
            // 一个商品的总价
            BigDecimal multiply = price.multiply(new BigDecimal(num));
            // 总价格
            sum = sum.add(multiply);
        }
        orders.setPayment(sum);
        ordersMapper.insert(orders);
        // 将购物车数据中设置订单ID后更新到数据库
        for (CartGoods cartGood : cartGoods) {
            cartGood.setOrderId(orders.getId());
            cartGoodsMapper.insert(cartGood);
        }
        //发送延时消息，30m后判断订单是否支付
        //在RocketMQ中，消息的延迟级别有18级 其中0代表不延迟，1-18依次对应下面的18个级别
        //"1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
        rocketMQTemplate.syncSend(
                RocketmqTopicEnum.CHECK_ORDERS_QUEUE.getTopicName(),
                MessageBuilder.withPayload(orders.getId()).build(),
                300000, 16);
        return orders;
    }

    /**
     * 修改订单
     * @param orders
     */
    @Override
    public void update(Orders orders) {
        ordersMapper.updateById(orders);
    }

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Override
    public Orders findById(String id) {
        return ordersMapper.findById(id);
    }

    /**
     * 查询用户的订单
     * @param userId
     * @param status
     * @return
     */
    @Override
    public List<Orders> findUserOrders(Long userId, Integer status) {
        return ordersMapper.findOrdersByUserIdAndStatus(userId, status);
    }
}
