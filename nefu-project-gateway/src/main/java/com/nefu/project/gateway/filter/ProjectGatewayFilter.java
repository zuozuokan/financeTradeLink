package com.nefu.project.gateway.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nefu.project.common.result.HttpResult;
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

/**
 * @description: 网关的全局过滤器
 */
@Slf4j
@Component
public class ProjectGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @SneakyThrows
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("来到网关的全局过滤器中...");
        ServerHttpRequest request = exchange.getRequest(); // 请求
        ServerHttpResponse response = exchange.getResponse(); //相应

        String path = request.getURI().getPath();
        System.err.println("RequestUrl:" + request.getURI());
        System.err.println("path:" + path);

        // 1. 获取请求地址，请求login() --放行
        if (path.contains("api/user/login") || path.contains("v3/api-docs")) {
            // 放行
            return chain.filter(exchange);
        }
        // 2. 获取token值
        HttpHeaders headers = request.getHeaders();
        List<String> tokenList = headers.get("login-token");
        ObjectMapper mapper = new ObjectMapper();
        DataBuffer dataBuffer = null;

        if (tokenList == null || tokenList.isEmpty()) { // token不存在
            // 异常抛出
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            HttpResult<String> result = HttpResult.failed("token不存在，用户登录异常");
            dataBuffer = response.bufferFactory().wrap(
                    mapper.writeValueAsString(
                                    result)
                            .getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(dataBuffer));
        }
        // 由于tokenList是利弊
        String token = tokenList.get(0);
        if (!stringRedisTemplate.hasKey(token)) {
            // 异常抛出
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            HttpResult<String> result = HttpResult.failed("非法用户，访问异常");
            dataBuffer = response.bufferFactory().wrap(
                    mapper.writeValueAsString(
                                    result)
                            .getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(dataBuffer));
        }


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }
}
