package com.tyut.shopping_order_service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
@EnableDubbo
@MapperScan("com.tyut.shopping_order_service.mapper")
@RefreshScope
public class ShoppingOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingOrderServiceApplication.class, args);
        log.info("******************* 订单服务模块启动成功 *******************");
    }

}
