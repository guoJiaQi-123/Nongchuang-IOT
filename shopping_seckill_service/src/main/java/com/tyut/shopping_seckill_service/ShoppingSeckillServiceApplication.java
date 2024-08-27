package com.tyut.shopping_seckill_service;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
@RefreshScope
@MapperScan("com.tyut.shopping_seckill_service.mapper")
@Slf4j
@EnableScheduling
public class ShoppingSeckillServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingSeckillServiceApplication.class, args);
        log.info("******************* 商品秒杀服务启动成功 *******************");
    }

    /**
     * 注册MybatisPlus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 注册布隆过滤器
     */
    @Bean
    public BitMapBloomFilter bitMapBloomFilter() {
        return new BitMapBloomFilter(1000); // 布隆过滤器中可以存放多少数据
    }
}
