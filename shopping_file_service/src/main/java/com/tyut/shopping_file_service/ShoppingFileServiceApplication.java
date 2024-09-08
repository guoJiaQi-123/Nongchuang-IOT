package com.tyut.shopping_file_service;

import com.github.tobato.fastdfs.FdfsClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
      
      
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
@EnableDiscoveryClient
@Import(FdfsClientConfig.class)
@RefreshScope
@Slf4j
public class ShoppingFileServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(ShoppingFileServiceApplication.class, args);
        log.info("******************* 文件系统启动成功 *******************");
    }

}
