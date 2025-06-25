package com.nefu.project.base;

import com.nefu.project.base.service.IInitAdminRoleService;
import com.nefu.project.base.service.impl.InitAdminRoleImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@ComponentScan(basePackages = {"com.nefu.project"})
public class BaseApp implements CommandLineRunner {

    @Autowired
    private IInitAdminRoleService initAdminRoleService;

    public static void main(String[] args) {
        SpringApplication.run(BaseApp.class);

    }

    @Override
    public void run(String... args) throws Exception {
        // 应用上下文初始化完成后自动调用
        initAdminRoleService.initAdmin();
    }
}
