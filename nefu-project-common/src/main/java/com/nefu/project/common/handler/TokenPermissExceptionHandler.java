package com.nefu.project.common.handler;

import com.nefu.project.common.exception.gateway.TokenPermissionException;
import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class TokenPermissExceptionHandler {
    @ExceptionHandler(TokenPermissionException.class)
    public HttpResult<String> tokenPermissionExceptionHandler(TokenPermissionException e){
        log.debug(e.getMessage());
        return HttpResult.failed(e.getMessage());
    }
}