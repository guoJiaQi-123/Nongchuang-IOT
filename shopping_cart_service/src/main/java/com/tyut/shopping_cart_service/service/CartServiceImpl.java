package com.tyut.shopping_cart_service.service;

import com.tyut.shopping_common.pojo.CartGoods;
import com.tyut.shopping_common.pojo.Category;
import com.tyut.shopping_common.service.CartService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @version v1.0
 * @author OldGj 2024/6/22
 * @apiNote 用户购物车业务层实现类
 */
@DubboService
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    // redis中哈希结构保存购物车数据的键
    private final String REDIS_HASH_KEY_CART = "cartList";

    /**
     * 向购物车中添加商品
     * 如果商品已经存在购物车，则修改商品数量
     * @param userId
     * @param cartGoods
     */
    @Override
    @SuppressWarnings("unchecked")
    public void addCart(Long userId, CartGoods cartGoods) {
        // 1. 根据用户ID查询该用户的购物车数据
        List<CartGoods> cartList = findCartList(userId);
        // 2. 查询用户购物车中是否已经存在当前商品，如果存在则更新商品数量即可
        for (CartGoods goods : cartList) {
            if (Objects.equals(goods.getGoodId(), cartGoods.getGoodId())) {
                // 商品数量为原来的商品数量+新增商品数量
                goods.setNum(goods.getNum() + cartGoods.getNum());
                // 更新redis中的商品数量
                redisTemplate.boundHashOps(REDIS_HASH_KEY_CART).put(userId, cartList);
                return;
            }
        }
        // 如果原先购物车没有该商品，则将该商品加入购物车
        cartList.add(cartGoods);
        redisTemplate.boundHashOps(REDIS_HASH_KEY_CART).put(userId, cartList);
    }


    /**
     * 修改购物车商品数量
     * @param userId
     * @param goodId
     * @param num
     */
    @Override
    @SuppressWarnings("unchecked")
    public void handleCart(Long userId, Long goodId, Integer num) {
        // 1. 根据用户ID获取当前用户的购物车数据
        List<CartGoods> cartList = findCartList(userId);
        // 2. 在购物车中查找当前商品
        for (CartGoods cartGoods : cartList) {
            if (cartGoods.getGoodId().equals(goodId)) {
                // 改变商品数量
                cartGoods.setNum(num);
                break;
            }
        }
        redisTemplate.boundHashOps(REDIS_HASH_KEY_CART).put(userId, cartList);
    }


    /**
     * 删除购物车商品
     * @param userId
     * @param goodId
     */
    @Override
    @SuppressWarnings("unchecked")
    public void deleteCartOption(Long userId, Long goodId) {
        List<CartGoods> cartList = findCartList(userId);
        cartList.removeIf(cartGoods -> cartGoods.getGoodId().equals(goodId));
        redisTemplate.boundHashOps(REDIS_HASH_KEY_CART).put(userId, cartList);
    }

    /**
     * 根据用户ID获取用户购物车
     * @param userId 用户ID
     * @return
     */
    @Override
    @SuppressWarnings("unchecked") // 忽略未检查的转化，例如集合没有指定类型的警告
    public List<CartGoods> findCartList(Long userId) {
        Object cartList = redisTemplate.boundHashOps(REDIS_HASH_KEY_CART).get(userId);
        if (cartList == null) {
            return new ArrayList<>();
        } else {
            return (List<CartGoods>) cartList;
        }
    }

    /**
     * 更新redis中的商品数据，在管理员更新商品后执行
     * @param cartGoods
     */
    @Override
    public void refreshCartGoods(CartGoods cartGoods) {
        // 所有用户的购物车
        BoundHashOperations cartList = redisTemplate.boundHashOps(REDIS_HASH_KEY_CART);
        Map<Long, List<CartGoods>> allCartGoods = cartList.entries();
        Collection<List<CartGoods>> values = allCartGoods.values();
        for (List<CartGoods> goodsList : values) {
            for (CartGoods goods : goodsList) {
                if (goods.getGoodId().equals(cartGoods.getGoodId())) {
                    goods.setGoodsName(cartGoods.getGoodsName());
                    goods.setPrice(cartGoods.getPrice());
                    goods.setHeaderPic(cartGoods.getHeaderPic());
                }
            }
        }
        redisTemplate.delete(REDIS_HASH_KEY_CART);
        redisTemplate.boundHashOps(REDIS_HASH_KEY_CART).putAll(allCartGoods);
    }

    /**
     * 删除redis中的商品数据，在管理员下架商品后执行
     * @param goodId
     */
    @Override
    public void deleteCartGoods(Long goodId) {
        // 获取所有用户购物车
        BoundHashOperations cartList = redisTemplate.boundHashOps(REDIS_HASH_KEY_CART);
        Map<Long, List<CartGoods>> allCartGoods = cartList.entries();
        Collection<List<CartGoods>> values = allCartGoods.values();

        // 遍历所有用户的购物车
        for (List<CartGoods> goodsList : values) {
            // 遍历一个用户购物车的所有商品
            for (CartGoods goods : goodsList) {
                // 如果该商品是被下架的商品，在购物车删除该商品
                if (goodId.equals(goods.getGoodId())) {
                    goodsList.remove(goods);
                    break;
                }
            }
        }

        // 将改变后所有用户购物车重新放入redis
        redisTemplate.delete(REDIS_HASH_KEY_CART);
        redisTemplate.boundHashOps(REDIS_HASH_KEY_CART).putAll(allCartGoods);
    }

    /**
     * 删除指定用户的购物车数据，在用户下单之和执行
     * @param goodId
     * @param userId
     */
    @Override
    public void deleteCartGoods(Long goodId, Long userId) {
        // 获取所有用户购物车
        BoundHashOperations cartList = redisTemplate.boundHashOps(REDIS_HASH_KEY_CART);
        Map<Long, List<CartGoods>> allCartGoods = cartList.entries();
        Set<Long> keySet = allCartGoods.keySet();
        for (Long id : keySet) {
            if (id.equals(userId)) {
                List<CartGoods> cartGoods = allCartGoods.get(userId);
                // 遍历一个用户购物车的所有商品
                for (CartGoods goods : cartGoods) {
                    // 如果该商品是被下架的商品，在购物车删除该商品
                    if (goodId.equals(goods.getGoodId())) {
                        cartGoods.remove(goods);
                        break;
                    }
                }
            }
        }

        // 将改变后所有用户购物车重新放入redis
        redisTemplate.delete(REDIS_HASH_KEY_CART);
        redisTemplate.boundHashOps(REDIS_HASH_KEY_CART).putAll(allCartGoods);
    }
}
