package com.nefu.project.common.handler;

import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class HttpMessageNotReadableExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public HttpResult<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return HttpResult.failed("价格等关键数字数据为空，数据更新失败，请确保相关数据不为空");
    }
}
