package com.nefu.project.common.handler;

import com.nefu.project.common.exception.gateway.TokenExpireException;
import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class TokenExpireExceptionHandler {
    @ExceptionHandler(TokenExpireException.class)
    public HttpResult<String> tokenExpireExceptionHandler(TokenExpireException e){
        log.debug(e.getMessage());
        return HttpResult.failed(e.getMessage());
    }
}
