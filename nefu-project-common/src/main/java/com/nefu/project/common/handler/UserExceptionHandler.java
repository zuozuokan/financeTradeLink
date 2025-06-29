package com.nefu.project.common.handler;


import com.nefu.project.common.exception.user.LoanApplicationException;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler{
    /** 
     * @description: 用户所有异常，具体写的时候需要分得更加细致 
     * @param:  
     * @return:  
     */
    @ExceptionHandler(UserException.class)
    public HttpResult<String> userExceptionHandler(UserException e){
        log.debug("用户出现异常:{}",e.getMessage());
        return HttpResult.failed(e.getMessage());
    }

}
