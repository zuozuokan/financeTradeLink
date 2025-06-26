package com.nefu.project.invest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.nefu.project"})
public class InvestApp {
    public static void main(String[] args) {
        SpringApplication.run(InvestApp.class, args);
    }
}
