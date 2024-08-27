package com.tyut.shopping_cart_service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
@EnableDiscoveryClient
@Slf4j
@RefreshScope
public class ShoppingCartServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(ShoppingCartServiceApplication.class, args);

		log.info("******************* 购物车服务模块启动成功 *******************");
    }

}
