package com.tyut.shopping_user_service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyut.shopping_common.pojo.ShoppingUser;
import com.tyut.shopping_common.result.BusException;
import com.tyut.shopping_common.result.CodeEnum;
import com.tyut.shopping_common.service.ShoppingUserService;
import com.tyut.shopping_common.util.Md5Util;
import com.tyut.shopping_user_service.mapper.ShoppingUserMapper;
import com.tyut.shopping_user_service.util.JwtUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @version v1.0
 * @author OldGj 2024/6/20
 * @apiNote 商城用户业务层实现类
 */
@DubboService
public class ShoppingUserServiceImpl implements ShoppingUserService {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ShoppingUserMapper shoppingUserMapper;

    /**
     * 注册时向redis保存手机号+验证码
     * @param phone
     * @param checkCode
     */
    @Override
    public void saveRegisterCheckCode(String phone, String checkCode) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 将手机号+验证码保存到redis中，其中key-手机号，value-验证码 过期时间五分钟
        valueOperations.set("registerCode:" + phone, checkCode, 60 * 5, TimeUnit.SECONDS);
    }


    /**
     * 注册时验证手机号+验证码
     * @param phone
     * @param checkCode
     */
    @Override
    public void registerCheckCode(String phone, String checkCode) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 从redis中获取验证码
        String checkCodeRedis = (String) valueOperations.get("registerCode:" + phone);
        if (!checkCode.equals(checkCodeRedis)) {
            throw new BusException(CodeEnum.REGISTER_CODE_ERROR);
        }
    }

    /**
     * 用户注册
     * @param shoppingUser
     */
    @Override
    public void register(ShoppingUser shoppingUser) {
        // 校验用户名和手机号是否重复
        String name = shoppingUser.getName();
        String phone = shoppingUser.getPhone();
        LambdaQueryWrapper<ShoppingUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingUser::getPhone, phone);
        List<ShoppingUser> shoppingUserList = shoppingUserMapper.selectList(lambdaQueryWrapper);
        if (!shoppingUserList.isEmpty() && shoppingUserList.size() > 0) {
            throw new BusException(CodeEnum.REGISTER_REPEAT_PHONE_ERROR);
        }
        LambdaQueryWrapper<ShoppingUser> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(ShoppingUser::getName, name);
        List<ShoppingUser> shoppingUserList1 = shoppingUserMapper.selectList(lambdaQueryWrapper1);
        if (!shoppingUserList1.isEmpty() && shoppingUserList1.size() > 0) {
            throw new BusException(CodeEnum.REGISTER_REPEAT_NAME_ERROR);
        }
        // 新增用户
        shoppingUser.setStatus("Y");
        shoppingUser.setPassword(Md5Util.encode(shoppingUser.getPassword()));
        shoppingUserMapper.insert(shoppingUser);
    }


    /**
     * 用户名密码登录
     * @param username 用户名
     * @param password 密码
     * @return 用户名
     */
    @Override
    public String loginPassword(String username, String password) {
        LambdaQueryWrapper<ShoppingUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingUser::getUsername, username);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(lambdaQueryWrapper);
        // 验证用户名
        if (shoppingUser == null) {
            throw new BusException(CodeEnum.LOGIN_NAME_PASSWORD_ERROR);
        }
        boolean verified = Md5Util.verify(password, shoppingUser.getPassword());
        if (!verified) {
            throw new BusException(CodeEnum.LOGIN_NAME_PASSWORD_ERROR);
        }
        // 返回jwt令牌
        String sign = JwtUtils.sign(shoppingUser.getId(), shoppingUser.getUsername());
        return sign;
    }

    /**
     * 登录时向redis保存手机号+验证码
     * @param phone
     * @param checkCode
     */
    @Override
    public void saveLoginCheckCode(String phone, String checkCode) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // redis键为手机号，值为验证码，过期时间5分钟
        valueOperations.set("loginCode:" + phone, checkCode, 5, TimeUnit.MINUTES);
    }

    /**
     * 手机号验证码登录
     * @param phone
     * @param checkCode
     * @return
     */
    @Override
    public String loginCheckCode(String phone, String checkCode) {
        // 验证用户传入的手机号验证码是否在redis中存在
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object checkCodeRedis = valueOperations.get("loginCode:" + phone);
        if (!checkCode.equals(checkCodeRedis)) {
            throw new BusException(CodeEnum.LOGIN_CHECK_CODE_ERROR);
        }
        LambdaQueryWrapper<ShoppingUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingUser::getPhone, phone);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(lambdaQueryWrapper);
        // 返回jwt令牌
        String sign = JwtUtils.sign(shoppingUser.getId(), shoppingUser.getUsername());
        return sign;
    }


    /**
     * 检查手机号是否存在，用户状态是否正常
     * @param phone
     */
    @Override
    public void checkUserPhone(String phone) {
        System.out.println("手机号" + phone);
        LambdaQueryWrapper<ShoppingUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingUser::getPhone, phone);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(lambdaQueryWrapper);
        if (shoppingUser == null) {
            throw new BusException(CodeEnum.LOGIN_NOT_PHONE_ERROR);
        }
        String status = shoppingUser.getStatus();
        if (!"Y".equals(status)) {
            throw new BusException(CodeEnum.LOGIN_STATE_ERROR);
        }
    }

    /**
     * 获取登录用户名
     * @param token
     * @return
     */
    @Override
    public String getName(String token) {
        Map<String, Object> verify = JwtUtils.verify(token);
        return (String) verify.get("username");
    }

    /**
     * 获取当前登录的用户
     * @param token
     * @return
     */
    @Override
    public ShoppingUser getLoginUser(String token) {
        // 获取用户ID
        Map<String, Object> verify = JwtUtils.verify(token);
        Long userId = (Long) verify.get("userId");
        // 根据用户ID查询用户
        return shoppingUserMapper.selectById(userId);
    }
}
