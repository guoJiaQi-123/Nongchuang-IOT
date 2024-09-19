package com.tyut.shopping_goods_service;

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

@SpringBootApplication
@EnableDiscoveryClient // 向注册中心注册该服务
@EnableDubbo // 开启dubbo
@RefreshScope // 配置动态刷新
@MapperScan("com.tyut.shopping_goods_service.mapper")
@Slf4j
public class ShoppingGoodsServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(ShoppingGoodsServiceApplication.class, args);
		log.info("******************* 商品服务启动成功 *******************");
	}

	/**
	 * 注册mybatis-plus的分页插件
	 * @return
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

}
