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

public class JwtUtil {

    public static final String SALT = "project_11_group";

    // 验证 token 是否合法
    public static boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SALT)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 提取 uuid
    public static String getUuidFromToken(String token) {
        return parseToken(token).getClaim("uuid").asString();
    }

    // 提取 role
    public static String getRoleFromToken(String token) {
        return parseToken(token).getClaim("role").asString();
    }

    // 解析 token
    private static DecodedJWT parseToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SALT)).build().verify(token);
        } catch (Exception e) {
            throw new TokenVerificationException("Token非法：" + e.getMessage());
        }
    }
}
