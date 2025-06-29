package com.nefu.project.gateway;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
        exclude = {
                DruidDataSourceAutoConfigure.class,
                DataSourceAutoConfiguration.class
        }
)
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.nefu.project"})
public class GatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class);
    }
}