package com.tyut.shopping_category_customer_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient //向注册中心注册该服务
@RefreshScope // 配置动态刷新
@Slf4j
@ComponentScan(value = {"com.tyut.shopping_common.component", "com.tyut.shopping_category_customer_api"})
public class ShoppingCategoryCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCategoryCustomerApiApplication.class, args);
        log.info("******************* 广告服务api模块启动成功 *******************");
    }

}
