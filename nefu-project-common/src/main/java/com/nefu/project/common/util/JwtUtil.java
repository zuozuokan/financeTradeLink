package com.nefu.project.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nefu.project.common.exception.gateway.TokenVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class JwtUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // token加盐
    public static final String SALT = "project_11_group";

    public static boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SALT))
                    .build()
                    .verify(token);
            return true;  // 验证通过
        } catch (Exception e) {
            return false; // 验证失败
        }
    }

    public static String getUuidFromToken(String token) {
        return parseToken(token).getClaim("uuid").asString();
    }

    public static String getRoleFromToken(String token) {
        return parseToken(token).getClaim("role").asString();
    }

    public boolean isTokenExpired(String token) {

        String userJson = stringRedisTemplate.opsForValue().get(token);
        if (Objects.isNull(userJson)) {
            return true;
        }
        return false;
    }
}
