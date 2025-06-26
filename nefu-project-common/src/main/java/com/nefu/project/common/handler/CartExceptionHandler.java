package com.nefu.project.common.handler;


import com.nefu.project.common.exception.user.CartException;
import com.nefu.project.common.exception.user.LoanApplicationException;
import com.nefu.project.common.result.HttpResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CartExceptionHandler {
    
    /**
     * description 购物车异常处理
     *
     * @params 
     * @return 
     */
    @ExceptionHandler(CartException.class)
    public HttpResult<String> CartExceptionHandler(Exception e){
        return HttpResult.failed(e.getMessage());
    }
}
