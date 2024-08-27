package com.tyut.shopping_common.service;

import com.tyut.shopping_common.result.BaseResult;

import java.util.concurrent.ExecutionException;

/**
 * @version v1.0
 * @apiNote 发送短信服务的api接口
 * @author OldGj 2024/6/20
 */
public interface MessageService {

    /**
     * 发送短信
     * @param phoneNumber 手机号
     * @param code 验证码
     * @return
     */
    BaseResult sendMessage(String phoneNumber, String code) throws ExecutionException, InterruptedException;


}
