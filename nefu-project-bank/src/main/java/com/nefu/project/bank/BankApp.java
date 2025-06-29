package com.nefu.project.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.nefu.project"})
public class BankApp {

    public static void main(String[] args) {
        SpringApplication.run(BankApp.class);
    }

}
