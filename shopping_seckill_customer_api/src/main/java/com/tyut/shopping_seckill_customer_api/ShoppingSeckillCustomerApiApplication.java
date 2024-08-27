package com.tyut.shopping_seckill_customer_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
@EnableDiscoveryClient
@RefreshScope
@ComponentScan(value = {"com.tyut.shopping_common.component", "com.tyut.shopping_seckill_customer_api"})
public class ShoppingSeckillCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingSeckillCustomerApiApplication.class, args);
        log.info("******************* 秒杀api启动成功 *******************");
    }

}
