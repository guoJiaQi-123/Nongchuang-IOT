package com.tyut.shopping_order_customer_api;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
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
@ComponentScan(value = {"com.tyut.shopping_common.component", "com.tyut.shopping_order_customer_api"})
public class ShoppingOrderCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingOrderCustomerApiApplication.class, args);
        log.info("******************* 订单api模块启动成功 *******************");
    }

}
