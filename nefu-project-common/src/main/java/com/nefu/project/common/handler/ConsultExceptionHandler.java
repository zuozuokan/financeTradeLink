package com.nefu.project.common.handler;



import com.nefu.project.common.exception.consult.ConsultException;
import com.nefu.project.common.result.HttpResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ConsultExceptionHandler {
    /**
     * 预约信息模块的错误
     */
    @ExceptionHandler(ConsultException.class)
    public HttpResult<String> UserConsultExceptionHandler(ConsultException e){
        return HttpResult.failed(e.getMessage());
    }
}