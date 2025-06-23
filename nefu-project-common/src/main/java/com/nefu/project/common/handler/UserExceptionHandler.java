package com.nefu.project.common.handler;


import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.common.result.HttpResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler{
    /** 
     * @description: 用户所有异常，具体写的时候需要分得更加细致 
     * @param:  
     * @return:  
     */
    @ExceptionHandler(UserException.class)
    public HttpResult<String> userExceptionHandler(UserException e){
        return HttpResult.failed(e.getMessage());
    }

}
