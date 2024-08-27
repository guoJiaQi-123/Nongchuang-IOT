package com.tyut.shopping_order_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyut.shopping_common.pojo.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version v1.0
 * @apiNote 订单数据持久层
 * @author OldGj 2024/6/23
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 根据ID查询订单详情
     * @param id
     * @return
     */
    Orders findById(String id);


    /**
     * 查询用户订单
     * @param userId
     * @param status
     * @return
     */
    List<Orders> findOrdersByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
}
