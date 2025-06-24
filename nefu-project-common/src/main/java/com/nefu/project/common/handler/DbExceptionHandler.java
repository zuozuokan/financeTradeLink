package com.nefu.project.common.handler;


import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DbExceptionHandler {
    //TODO: 处理SQL异常
    @ExceptionHandler(DbException.class)
    public HttpResult<String> userExceptionHandler(DbException e){
        log.debug("数据库出现异常:{}",e.getMessage());
        return HttpResult.failed(e.getMessage());
    }
}
