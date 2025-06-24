package com.nefu.project.common.handler;

import com.nefu.project.common.exception.user.UserRegistryException;
import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class UserRegistryExceptionHandler {

    /**
     * @description: 处理用户注册异常
     * @param:
     * @return:
     */
    @ExceptionHandler(UserRegistryException.class)
    public HttpResult handleUserRegistryException(UserRegistryException e){
        log.debug("用户注册异常，异常信息为：{}", e.getMessage());
        return HttpResult.failed(e.getMessage());
    }
}
