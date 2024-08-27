package com.tyut.shopping_common.service;

import com.tyut.shopping_common.pojo.ShoppingUser;

/**
 * @version v1.0
 * @author OldGj 2024/6/20
 * @apiNote 商城用户服务接口
 */
public interface ShoppingUserService {
    /**
     * 注册时向redis保存手机号+验证码
     * @param phone
     * @param checkCode
     */
    void saveRegisterCheckCode(String phone, String checkCode);


    /**
     * 注册时验证手机号+验证码
     * @param phone
     * @param checkCode
     */
    void registerCheckCode(String phone, String checkCode);


    /**
     * 用户注册
     * @param shoppingUser
     */
    void register(ShoppingUser shoppingUser);


    /**
     * 用户名密码登录
     * @param username
     * @param password
     * @return
     */
    String loginPassword(String username, String password);

    /**
     * 登录时向redis保存手机号+验证码
     * @param phone
     * @param checkCode
     */
    void saveLoginCheckCode(String phone, String checkCode);


    /**
     * 手机号验证码登录
     * @param phone
     * @param checkCode
     * @return
     */
    String loginCheckCode(String phone, String checkCode);

    /**
     * 获取登录用户名
     * @param token
     * @return
     */
    String getName(String token);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    ShoppingUser getLoginUser(String token);


    /**
     * 检查手机号是否存在，用户状态是否正常
     * @param phone
     */
    void checkUserPhone(String phone);
}
