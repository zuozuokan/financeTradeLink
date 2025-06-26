package com.nefu.project.gateway.filter;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nefu.project.common.exception.gateway.TokenExpireException;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.common.util.JwtService;
import com.nefu.project.common.util.JwtUtil;
import com.nefu.project.domain.entity.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @description: 网关的全局过滤器
 */
@Slf4j
@Component
public class ProjectGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JwtService jwtService;

    @Override
    @SneakyThrows
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("来到网关的全局过滤器中...");
        ServerHttpRequest request = exchange.getRequest(); // 请求
        ServerHttpResponse response = exchange.getResponse(); //响应

        String path = request.getURI().getPath();
        System.err.println("RequestUrl:" + request.getURI());
        System.err.println("path:" + path);

        // 获取请求地址，请求login() --放行，请求register() -- 放行
        if (path.contains("api/login") || path.contains("api/register") || path.contains("v3/api-docs")) {
            // 放行
            return chain.filter(exchange);
        }
        // 获取token值
        HttpHeaders headers = request.getHeaders();
        List<String> tokenList = headers.get("token");
        ObjectMapper mapper = new ObjectMapper();
        DataBuffer dataBuffer = null;

        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        if (Objects.isNull(tokenList) || tokenList.isEmpty()) { // token不存在
            // 异常抛出
            HttpResult<String> result = HttpResult.failed("token不存在，用户登录异常");
            dataBuffer = response.bufferFactory().wrap(
                    mapper.writeValueAsString(
                                    result)
                            .getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(dataBuffer));
        }
        // tokenList是列表，获取第一个值
        String token = tokenList.get(0);

        // 解析token,若验证错误,则抛出错误
        boolean isTokenValid = JwtUtil.isTokenValid(token);
        if (!isTokenValid) {
            // 异常抛出
            HttpResult<String> tokenError = HttpResult.failed("token不合法");
            dataBuffer = response.bufferFactory().wrap(
                    mapper.writeValueAsString(
                                    tokenError)
                            .getBytes(StandardCharsets.UTF_8));
            log.debug("token验证错误");
            return response.writeWith(Mono.just(dataBuffer));
        }

        // 判断token是否过期
        boolean isExpired =  jwtService.isTokenExpired(token);
        if (isExpired) {
            // 异常抛出
            HttpResult<String> tokenExpired = HttpResult.failed("token已过期，请重新登录");
            dataBuffer = response.bufferFactory().wrap(
                    mapper.writeValueAsString(
                                    tokenExpired)
                            .getBytes(StandardCharsets.UTF_8));
            log.debug("token已过期");
            return response.writeWith(Mono.just(dataBuffer));
        }

        // 提取用户角色和Uuid
        String userRole = JwtUtil.getRoleFromToken(token);
        String userUuid = JwtUtil.getUuidFromToken(token);

        // 判断管理员权限
        if (path.contains("api/admin")) {
            if (!userRole.equals(User.Role.ADMIN.toString())) {
                // 异常抛出
                HttpResult<String> result = HttpResult.failed("非法用户，无权限访问");
                dataBuffer = response.bufferFactory().wrap(
                        mapper.writeValueAsString(
                                        result)
                                .getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(dataBuffer));
            }else{
                return chain.filter(exchange);
            }
        }

        // 判断普通用户权限
        if (path.contains("api/user")) {
            if (!userRole.equals(User.Role.USER.toString())) {
                // 异常抛出
                HttpResult<String> result = HttpResult.failed("非法用户，无权限访问");
                dataBuffer = response.bufferFactory().wrap(
                        mapper.writeValueAsString(
                                        result)
                                .getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(dataBuffer));
            }else
                return chain.filter(exchange);
        }

        // 判断银行用户权限
        if (path.contains("api/bank")) {
            if (!userRole.equals(User.Role.BANK.toString())) {
                // 异常抛出
                HttpResult<String> result = HttpResult.failed("非法用户，无权限访问");
                dataBuffer = response.bufferFactory().wrap(
                        mapper.writeValueAsString(
                                        result)
                                .getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(dataBuffer));
            }else
                return chain.filter(exchange);
        }

        // 判断专家用户权限
        if (path.contains("api/expert")) {
            if (!userRole.equals(User.Role.EXPERT.toString())) {
                // 异常抛出
                HttpResult<String> result = HttpResult.failed("非法用户，无权限访问");
                dataBuffer = response.bufferFactory().wrap(
                        mapper.writeValueAsString(
                                        result)
                                .getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(dataBuffer));
            }else
                return chain.filter(exchange);
        }


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }
}
