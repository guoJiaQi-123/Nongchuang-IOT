package com.tyut.shopping_user_customer_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@RefreshScope
@Slf4j
@ComponentScan(value = {"com.tyut.shopping_common.component","com.tyut.shopping_user_customer_api"})
public class ShoppingUserCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingUserCustomerApiApplication.class, args);
        log.info("******************* 商城用户api模块启动成功 *******************");
    }

}
