package com.tyut.shopping_common.service;

import com.tyut.shopping_common.pojo.CartGoods;

import java.util.List;

// 购物车服务
public interface CartService {
    /**
     * 新增商品到购物车
     * @param userId
     * @param cartGoods
     */
    void addCart(Long userId, CartGoods cartGoods);


    /**
     * 修改购物车商品数量
     * @param userId
     * @param goodId
     * @param num
     */
    void handleCart(Long userId, Long goodId, Integer num);


    /**
     * 删除购物车商品
     * @param userId
     * @param goodId
     */
    void deleteCartOption(Long userId, Long goodId);


    /**
     * 获取用户购物车
     * @param userId
     * @return
     */
    List<CartGoods> findCartList(Long userId);


    /**
     * 更新redis中的商品数据，在管理员更新商品后执行
     * @param cartGoods
     */
    void refreshCartGoods(CartGoods cartGoods);


    /**
     * 删除redis中的商品数据，在管理员下架商品后执行
     * @param goodId
     */
    void deleteCartGoods(Long goodId);


    /**
     * 删除指定用户的购物车数据，在用户下单之和执行
     * @param goodId
     * @param userId
     */
    void deleteCartGoods(Long goodId,Long userId);
}