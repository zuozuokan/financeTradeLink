package com.nefu.project.user.controller;

import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.User;
import com.nefu.project.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户模块接口api")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IUserService iUserService;

    @Operation(summary = "测试找到所有用户")
    @SneakyThrows
    @GetMapping("/find-all-users")
    public HttpResult findAllUsers() {
        List<User> users = iUserService.findAllUsers();
        return HttpResult.success(users);
    }

}
