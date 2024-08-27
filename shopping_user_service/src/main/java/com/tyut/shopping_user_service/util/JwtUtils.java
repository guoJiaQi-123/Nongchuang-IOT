package com.tyut.shopping_user_service.util;

import lombok.SneakyThrows;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    // 公钥
    public final static String PUBLIC_JSON = "{\"kty\":\"RSA\",\"n\":\"poP-6f4t6iBF7gITxTlrGfk8v7Kztkg6hWDjFB8iAGU-y2K2va1Pp2WdhkxUF4KSS7ZcOynH5RgEipZV99fIi-iT-HuXka1tw72TWfS5ukil2hRhl5u4MoubqaIiEHCqSdZEUpI0Qb8vMiYC0e-rYGMgPhpXztPh43AZF8ZON9bONdm1pNBvhhu2g9c3Sc_Wg5fPETIml83BhO3nNTZ802EM1o-4xApsmV2n8c76WcJnDUFySEy8-P1q-W5esiQBX2WkgDz_PoOtk4C1AOwo89cZ9cq8z8XYbAka5ZzM728moZ4j220bcE8kxVyFXVS5Ng2W2iovF29Pcq8eAJCeBw\",\"e\":\"AQAB\"}";
    // 私钥
    public final static String PRIVATE_JSON = "{\"kty\":\"RSA\",\"n\":\"poP-6f4t6iBF7gITxTlrGfk8v7Kztkg6hWDjFB8iAGU-y2K2va1Pp2WdhkxUF4KSS7ZcOynH5RgEipZV99fIi-iT-HuXka1tw72TWfS5ukil2hRhl5u4MoubqaIiEHCqSdZEUpI0Qb8vMiYC0e-rYGMgPhpXztPh43AZF8ZON9bONdm1pNBvhhu2g9c3Sc_Wg5fPETIml83BhO3nNTZ802EM1o-4xApsmV2n8c76WcJnDUFySEy8-P1q-W5esiQBX2WkgDz_PoOtk4C1AOwo89cZ9cq8z8XYbAka5ZzM728moZ4j220bcE8kxVyFXVS5Ng2W2iovF29Pcq8eAJCeBw\",\"e\":\"AQAB\",\"d\":\"RDUbTQP2EMopeuXU7VuouFn0fV2Y6ZjKh-n4-jwKdHkEcNE9o5KDZ0Fjdih75alxfZv0SbPCkt_0tEQCcOQt3MlnG0ic_Go65QwZeKabEWhCr4LbuvtpRyMSnzivlWZhCp-_GseSqj_C-FrDmvT-kRvpkCbAQxNdmwlG7gsn7QzhJpctfJW6byYFRaaPmGuRwriiXIJixlNlQcghLbQOAf1gyc5WMt734F6X3XV6oUNqGFoh3CQpNPtzud7V1Z0SYKKcd2Ma-hhAipJTspGponlamcjuDER3YsEDWnUr1GfHq1PX0jDEWHVzsA7g7gJAlRm3wd23Xtto3WjyaajhAQ\",\"p\":\"5GmHVErQV-G21gRQH81fFN2r81n1m-SpgU6KWRrkaj-KqZdDcVkjRcQN01yP92LA2HL8nqQGxbLp9rXEkkVKJqzd36V1ns84MoUZwPpPMfnsSrhOfwNAt9oQF5P7m-xB-xOAI9Wg0Sxcm9WkWAoAiLcn6zh93VoOuFUP4eqohYc\",\"q\":\"uqCiGjatFr83EghOsTDwycBLdUkpiIweJF_gmC8nr9HlQbw2OL1Gd_u6k6GaEAguLuPFnId7tXGcs8qkqUyLDAf0uppp0qXx3NoY4xmbGm49f1gnMHwFE_1k_ZooPaID-4K3bVEoPilrZ7n1VT7GzLlteilcDPWCdzMeU2B1Q4E\",\"dp\":\"eO1p6XWmcjCdBRfJd9zaLwjhRXhmMT_RghUb5-r17U5w0jK0USlZJFGU5EcILXhKEw5lsnOy5i4_8g7v4GTwyiMRYJuP9yFMZmrTZLjg_tuLf0-ut034kJ3RkToWpslhcVU0rUQn3TZ4XUSxuq6o87jMWJtS_8LzMiJOw1PiHrc\",\"dq\":\"RmlQf39dcpWWG3GS7vs9_YBVd9ywlNn4jVS3EfPIj2crjc-KzYFr0tzgmc6Ap8fOVNaOue3L2LsSmiq8UTuVwmZGCRk1D8qYI_ENLrF8eU5aWW9S6dIAwHf74EqHICm-QuggeFgETN-nrCzWyOC0dI0JJuvv1NvT61EYAfEEjQE\",\"qi\":\"Xjl2oUbIo4jJ-qDTWRu9CCHqLL_AKJk4jCY2ujX6437FStPiQP-byozfkTL5MO7-Ogubzd5Y2Sth97ldrc-YnNyES2WuuRNpg6aeHYpnCkspGZnLepbnD5twFqC9u2ruz-3DAbit75VJhMWTYcmTwrtnrroLl4U6-nGXBhEJgE0\"}";


    /**
     * 生成token
     *
     * @param userId    用户id
     * @param username 用户名字
     * @return
     */
    @SneakyThrows
    public static String sign(Long userId, String username) {
        // 1、 创建jwtclaims  jwt内容载荷部分
        JwtClaims claims = new JwtClaims();
        // 是谁创建了令牌并且签署了它
        claims.setIssuer("guojiaqi");
        // 令牌将被发送给谁
        claims.setAudience("audience");
        // 失效时间长 （分钟）
        claims.setExpirationTimeMinutesInTheFuture(60 * 24);
        // 令牌唯一标识符
        claims.setGeneratedJwtId();
        // 当令牌被发布或者创建现在
        claims.setIssuedAtToNow();
        // 再次之前令牌无效
        claims.setNotBeforeMinutesInThePast(2);
        // 主题
        claims.setSubject("shopping");
        // 可以添加关于这个主题得声明属性
        claims.setClaim("userId", userId);
        claims.setClaim("username", username);


        // 2、签名
        JsonWebSignature jws = new JsonWebSignature();
        //赋值载荷
        jws.setPayload(claims.toJson());


        // 3、jwt使用私钥签署
        PrivateKey privateKey = new RsaJsonWebKey(JsonUtil.parseJson(PRIVATE_JSON)).getPrivateKey();
        jws.setKey(privateKey);


        // 4、设置关键 kid
        jws.setKeyIdHeaderValue("keyId");


        // 5、设置签名算法
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        // 6、生成jwt
        String jwt = jws.getCompactSerialization();
        return jwt;
    }


    /**
     * 解密token，获取token中的信息
     *
     * @param token
     */
    @SneakyThrows
    public static Map<String, Object> verify(String token) {
        // 1、引入公钥
        PublicKey publicKey = new RsaJsonWebKey(JsonUtil.parseJson(PUBLIC_JSON)).getPublicKey();
        // 2、使用jwtcoonsumer  验证和处理jwt
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() //过期时间
                .setAllowedClockSkewInSeconds(30) //允许在验证得时候留有一些余地 计算时钟偏差  秒
                .setRequireSubject() // 主题生命
                .setExpectedIssuer("guojiaqi") // jwt需要知道谁发布得 用来验证发布人
                .setExpectedAudience("audience") //jwt目的是谁 用来验证观众
                .setVerificationKey(publicKey) // 用公钥验证签名  验证密钥
                .setJwsAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256))
                .build();
        // 3、验证jwt 并将其处理为 claims
        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            return jwtClaims.getClaimsMap();
        } catch (Exception e) {
            return new HashMap();
        }
    }


    public static void main(String[] args) {
        // 生成
        String baizhan = sign(1001L, "baizhan");
        System.out.println(baizhan);


        Map<String, Object> stringObjectMap = verify(baizhan);
        System.out.println(stringObjectMap.get("userId"));
        System.out.println(stringObjectMap.get("username"));
    }
}