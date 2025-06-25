package com.nefu.project.common.handler;

import com.nefu.project.common.exception.order.StockException;
import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class OrderExceptionHandler {
    @ExceptionHandler(StockException.class)
    public HttpResult<String> stockException(StockException e) {
        log.error(e.getMessage());
        return HttpResult.failed(e.getMessage());
    }
}
