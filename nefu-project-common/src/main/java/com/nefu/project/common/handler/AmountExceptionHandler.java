package com.nefu.project.common.handler;

import com.nefu.project.common.exception.order.AmountException;
import com.nefu.project.common.exception.user.CartException;
import com.nefu.project.common.result.HttpResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AmountExceptionHandler {
    @ExceptionHandler(AmountException.class)
    public HttpResult<String> AmountExceptionHandler(Exception e){
        return HttpResult.failed(e.getMessage());
    }
}
