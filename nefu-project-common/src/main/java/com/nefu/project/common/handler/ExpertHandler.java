package com.nefu.project.common.handler;


import com.nefu.project.common.exception.Expert.ExpertException;
import com.nefu.project.common.result.HttpResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExpertHandler {
    /**
     * 专家模块的错误
     */
    @ExceptionHandler(ExpertException.class)
    public HttpResult<String> ExpertExceptionHandler(ExpertException e){
        return HttpResult.failed(e.getMessage());
    }
}