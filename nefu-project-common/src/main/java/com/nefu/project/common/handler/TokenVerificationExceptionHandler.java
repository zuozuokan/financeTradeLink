package com.nefu.project.common.handler;

import com.nefu.project.common.exception.gateway.TokenVerificationException;
import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class TokenVerificationExceptionHandler {

    @ExceptionHandler(TokenVerificationException.class)
    public HttpResult<String> tokenVerificationException(TokenVerificationException e){
        log.debug("Token令牌错误:{}",e.getMessage());
        return HttpResult.failed(e.getMessage());
    }
}
