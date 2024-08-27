package com.tyut.shopping_order_customer_api.controller;

import com.tyut.shopping_common.pojo.CartGoods;
import com.tyut.shopping_common.pojo.Orders;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.CartService;
import com.tyut.shopping_common.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/23
 * @apiNote 订单控制层
 */
@RestController
@Slf4j
@RequestMapping("/user/orders")
public class OrdersController {


    @DubboReference
    private CartService cartService; // 购物车服务
    @DubboReference(retries = 0)
    private OrdersService ordersService;

    /**
     * 生成订单
     * @param orders 订单对象
     * @param userId 用户Id
     * @return 生成的订单
     */
    @PostMapping("/add")
    public BaseResult<Orders> add(@RequestHeader Long userId, @RequestBody Orders orders) {
        orders.setUserId(userId);
        // 保存订单
        Orders orders1 = ordersService.add(orders); // 操作1
        List<CartGoods> cartGoods = orders.getCartGoods();
        // 将redis中购物车中的商品删除
        for (CartGoods cartGood : cartGoods) {
            cartService.deleteCartGoods(cartGood.getGoodId(), userId); // 操作2
        }
        return BaseResult.ok(orders1);
    }


    /**
     * 根据id查询订单
     * @param id 订单id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseResult<Orders> findById(String id) {
        Orders orders = ordersService.findById(id);
        return BaseResult.ok(orders);
    }

    /**
     * 查询用户的订单
     * @param status 订单状态：1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭 7.待评价，传入空值代表查询所有
     * @param userId 用户id
     * @return 查询结果
     */
    @GetMapping("/findUserOrders")
    public BaseResult<List<Orders>> findUserOrders(Integer status, @RequestHeader Long userId) {
        List<Orders> orders = ordersService.findUserOrders(userId, status);
        return BaseResult.ok(orders);
    }

}
