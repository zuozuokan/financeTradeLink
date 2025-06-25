package com.nefu.project.base.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nefu.project.base.service.ILoginService;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Tag(name = "登录接口")
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private ILoginService iLoginService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //数字签名
    private final String SALT = "project_11_group";

    @SneakyThrows
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public HttpResult login(String userName,String password,String role){
        // 传回的用户不为空
        User user = iLoginService.login(userName,password,role);
        // 生成一个token字符串
        String token = JWT.create()
                .withClaim("uuid", user.getUserUuid()) // 用户名
                .withClaim("role", user.getUserRole()) // 角色信息
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 过期时间(默认过期时间15分钟)
                .sign(Algorithm.HMAC256(SALT));  //数字签名

        // 将用户信息存入redis
        ObjectMapper mapper = new ObjectMapper();
        String userStr = mapper.writeValueAsString(user);

        // 存入redis,加入随机时间,防止缓存雪崩
        Random random = new Random();
        stringRedisTemplate.opsForValue().set(token,userStr,30 + random.nextInt(10), TimeUnit.MINUTES);

        return HttpResult.success(token);
    }
}
