package com.nefu.project.common.handler;

import com.nefu.project.common.exception.user.AdminException;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AdminExceptionHandler {

    @ExceptionHandler(AdminException.class)
    public HttpResult<String> userExceptionHandler(AdminException e){
        log.debug("管理员操作出现异常:{}",e.getMessage());
        return HttpResult.failed(e.getMessage());
    }
}
