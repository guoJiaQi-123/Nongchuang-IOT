package com.tyut.shopping_cart_customer_api;

import com.tyut.shopping_common.component.TakeTimeCountListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RefreshScope
@EnableDiscoveryClient
@Slf4j
@ComponentScan(value = {"com.tyut.shopping_common.component", "com.tyut.shopping_cart_customer_api"}) // 注册bean容器
public class ShoppingCartCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartCustomerApiApplication.class, args);
        log.info("******************* 购物车api模块启动成功 *******************");
    }

}
