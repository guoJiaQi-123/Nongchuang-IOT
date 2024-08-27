package com.tyut.shopping_common.enu;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * rocketMQ主题枚举
 */
@Getter
@AllArgsConstructor
public enum RocketmqTopicEnum {
    /**
     * 同步商品数据消息到ES队列
     */
    SYNC_GOODS_QUEUE("sync_goods_queue"),

    /**
     * 删除ES中的商品数据消息队列
     */
    DEL_GOODS_QUEUE("del_goods_queue"),

    /**
     * 同步购物车数据到redis消息队列
     */
    SYNC_CART_QUEUE("sync_cart_queue"),

    /**
     * 删除购物车数据到redis消息队列
     */
    DEL_CART_QUEUE("del_cart_queue"),

    /**
     * 订单生成后，将订单ID放入一个延迟队列，订单过期消费
     */
    CHECK_ORDERS_QUEUE("check_orders_queue");


    private final String topicName;

}
