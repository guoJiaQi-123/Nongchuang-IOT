package com.tyut.shopping_user_customer_api.controller;

import com.tyut.shopping_common.pojo.ShoppingUser;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.MessageService;
import com.tyut.shopping_common.service.ShoppingUserService;
import com.tyut.shopping_common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;


/**
 * @version v1.0
 * @author OldGj 2024/6/20
 * @apiNote 商城用户控制器
 */
@RestController
@RequestMapping("/user/shoppingUser")
@Slf4j
public class ShoppingUserController {

    @DubboReference(timeout = 60 * 1000, retries = 1) // 超时时间60,秒，重试一次
    private ShoppingUserService shoppingUserService; // 商城用户服务
    @DubboReference(timeout = 1000 * 60, retries = 0) // 超时时间60秒，不重试
    private MessageService messageService; // 短信服务

    /**
     * 发送短信
     * @param phone 手机号
     * @return
     */
    @GetMapping("/sendMessage")
    public BaseResult sendMessage(String phone) throws ExecutionException, InterruptedException {

        // 1.生成验证码
        String checkCode = RandomUtil.buildCheckCode(4);
        log.info("给手机号【{}】，发送验证码{}", phone, checkCode);
        // 2.发送短信
        BaseResult<String> smsResult = messageService.sendMessage(phone, checkCode);
        // 3.发送成功，将验证码保存到redis中,发送失败，返回发送结果
        if (200 == smsResult.getCode()) {
            // 如果短信发送成功，将验证码和手机号信息保存到redis中
            shoppingUserService.saveRegisterCheckCode(phone, checkCode);
            return BaseResult.ok();
        } else {
            return smsResult;
        }
    }


    /**
     * 验证用户注册验证码
     * @param phone
     * @param checkCode
     * @return
     */
    @GetMapping("/registerCheckCode")
    public BaseResult registerCheckCode(String phone, String checkCode) {
        shoppingUserService.registerCheckCode(phone, checkCode);
        return BaseResult.ok();
    }


    /**
     * 用户注册
     * @param shoppingUser
     * @return 注册结果
     */
    @PostMapping("/register") // 注册使用post请求，可以隐藏秘密，安全性高
    public BaseResult register(@RequestBody ShoppingUser shoppingUser) {
        shoppingUserService.register(shoppingUser);
        return BaseResult.ok();
    }


    /**
     * 用户名密码登录
     * @param shoppingUser 用户对象
     * @return 登录结果
     */
    @PostMapping("/loginPassword")
    public BaseResult login(@RequestBody ShoppingUser shoppingUser) {
        String sign = shoppingUserService.loginPassword(shoppingUser.getUsername(), shoppingUser.getPassword());
        return BaseResult.ok(sign);
    }

    /**
     * 发送登录短信验证码
     *
     * @param phone 手机号
     * @return 操作结果
     */
    @GetMapping("/sendLoginCheckCode")
    public BaseResult sendLoginCheckCode(String phone) throws ExecutionException, InterruptedException {
        // 查看手机号是否存在以及用户状态是否正常
        shoppingUserService.checkUserPhone(phone);
        // 1.生成随机四位数
        String checkCode = RandomUtil.buildCheckCode(4);
        // 2.发送短信
        BaseResult result = messageService.sendMessage(phone, checkCode);
        // 3.发送成功，将验证码保存到redis中,发送失败，返回发送结果
        if (200 == result.getCode()) {
            shoppingUserService.saveLoginCheckCode(phone, checkCode);
            return BaseResult.ok();
        } else {
            return result;
        }
    }

    /**
     * 验证登录验证码是否正确
     * @param phone 手机号
     * @param checkCode 验证码
     * @return
     */
    @PostMapping("/loginCheckCode")
    public BaseResult loginCheckCode(String phone, String checkCode) {
        String sign = shoppingUserService.loginCheckCode(phone, checkCode);
        return BaseResult.ok("Bearer " + sign);
    }

    /**
     * 在jwt令牌中获取用户名
     * @param authorization
     * @return
     */
    @GetMapping("/getName")
    public BaseResult<String> getName(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        String name = shoppingUserService.getName(token);
        return BaseResult.ok(name);
    }

}
