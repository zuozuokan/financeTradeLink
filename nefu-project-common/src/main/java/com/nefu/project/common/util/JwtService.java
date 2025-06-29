package com.nefu.project.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JwtService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 判断是否 token 过期（在 Redis 中是否还有记录）
    public boolean isTokenExpired(String token) {
        String userJson = stringRedisTemplate.opsForValue().get(token);
        return Objects.isNull(userJson);
    }
}

