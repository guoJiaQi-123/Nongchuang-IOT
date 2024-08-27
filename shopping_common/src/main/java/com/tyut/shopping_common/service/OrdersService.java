package com.tyut.shopping_common.service;

import com.tyut.shopping_common.pojo.Orders;

import java.util.List;

public interface OrdersService {
    /**
     * 生成订单
     * @param orders
     * @return
     */
    Orders add(Orders orders);

    /**
     * 修改订单
     * @param orders
     */
    void update(Orders orders);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    Orders findById(String id);

    /**
     * 查询用户的订单
     * @param userId
     * @param status
     * @return
     */
    List<Orders> findUserOrders(Long userId, Integer status);
}