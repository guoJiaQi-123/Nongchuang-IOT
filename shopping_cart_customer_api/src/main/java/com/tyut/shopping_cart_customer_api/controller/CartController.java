package com.tyut.shopping_cart_customer_api.controller;

import com.tyut.shopping_common.pojo.CartGoods;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/22
 * @apiNote 购物车控制层
 */
@RestController
@RequestMapping("/user/cart")
@Slf4j
public class CartController {

    @DubboReference
    private CartService cartService;


    /**
     * 查询购物车数据
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/findCartList")
    public BaseResult<List<CartGoods>> findCartList(@RequestHeader("userId") Long userId) {
        log.info("用户ID：{}", userId);
        List<CartGoods> cartList = cartService.findCartList(userId);
        return BaseResult.ok(cartList);
    }

    /**
     * 向购物车中添加商品
     * @param cartGoods
     * @param userId
     * @return
     */
    @PostMapping("/addCart")
    public BaseResult addCart(@RequestBody CartGoods cartGoods, @RequestHeader("userId") Long userId) {
        cartService.addCart(userId, cartGoods);
        return BaseResult.ok();
    }

    /**
     * 修改购物车商品数量
     * @param userId 令牌中携带的用户Id
     * @param goodId 商品id
     * @param num 修改后的数量
     * @return 操作结果
     */
    @GetMapping("/handleCart")
    public BaseResult addCart(@RequestHeader("userId") Long userId, Long goodId, Integer num) {
        cartService.handleCart(userId, goodId, num);
        return BaseResult.ok();
    }

    /**
     * 删除购物车商品
     * @param userId 令牌中携带的用户Id
     * @param goodId 商品id
     * @return 操作结果
     */
    @DeleteMapping("/deleteCart")
    public BaseResult deleteCart(@RequestHeader("userId") Long userId, Long goodId) {
        cartService.deleteCartOption(userId, goodId);
        return BaseResult.ok();
    }


    /**
     * 测试请求头中是否封装了jwt中加入的参数
     * @return
     */
    @GetMapping("/test")
    public BaseResult<String> test(@RequestHeader("userId") Long userId, @RequestHeader("username") String username) {
        System.out.println(username);
        System.out.println(userId);
        return BaseResult.ok();
    }

}
