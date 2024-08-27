package com.tyut.shopping_message_service.service;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.MessageService;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @version v1.0
 * @author OldGj 2024/6/20
 * @apiNote 短信服务实现类
 */
@Slf4j
@DubboService
public class MessageServiceImpl implements MessageService {

    @Value("${message.accessKeyId}")
    private String accessKeyId;
    @Value("${message.accessKeySecret}")
    private String accessKeySecret;


    /**
     * 发送短信
     * @param phoneNumber 接收短信的手机号
     * @param code 验证码
     * @return
     */
    @Override
    public BaseResult sendMessage(String phoneNumber, String code) throws ExecutionException, InterruptedException {
        log.info("给手机号 【{}】发送短信，验证码内容：{}", phoneNumber, code);
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                // Please ensure that the environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and ALIBABA_CLOUD_ACCESS_KEY_SECRET are set.
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                //.securityToken(System.getenv("ALIBABA_CLOUD_SECURITY_TOKEN")) // use STS token
                .build());

        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou") // Region ID
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                        //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();

        // Parameter settings for API request
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("阿里云短信测试")
                .templateCode("SMS_154950909")
                .phoneNumbers(phoneNumber)
                .templateParam("{\"code\":\"" + code + "\"}")
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        // Synchronously get the return value of the API request
        SendSmsResponse resp = response.get();

        client.close();
        SendSmsResponseBody respBody = resp.getBody();
        if ("OK".equals(respBody.getCode())) {
            log.info("******************* 短信发送成功 *******************");
            return BaseResult.builder()
                    .code(200)
                    .message(respBody.getCode())
                    .data(respBody.getMessage())
                    .build();
        } else {
            log.error("******************* 短信发送失败 *******************");
            return BaseResult.builder()
                    .code(500)
                    .message(respBody.getCode())
                    .data(respBody.getMessage())
                    .build();
        }
    }
}
