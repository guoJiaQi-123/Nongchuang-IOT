package com.tyut.shopping_pay_service;

import com.alipay.api.AlipayApiException;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class ZfbPayConfig {
    // 应用id
    private String appId;
    // 应用私钥
    private String privateKey;
    // 支付宝公钥
    private String publicKey;
    // 网关
    private String gateway;
    // 回调网址
    private String notifyUrl;
    // 支付成功回调接口
    private String pcNotify;


    /**
     * 设置支付宝客户端
     */
    @Bean
    public AlipayClient setAlipayClient() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(gateway);
        //设置应用ID
        alipayConfig.setAppId(appId);
        //设置应用私钥
        alipayConfig.setPrivateKey(privateKey);
        //设置请求格式，固定值json
        alipayConfig.setFormat("json");
        //设置字符集
        alipayConfig.setCharset("utf-8");
        //设置签名类型
        alipayConfig.setSignType("RSA2");
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(publicKey);
        //实例化客户端
        return new DefaultAlipayClient(alipayConfig);
    }
}
