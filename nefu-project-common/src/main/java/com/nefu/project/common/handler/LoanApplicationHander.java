package com.nefu.project.common.handler;

import com.nefu.project.common.exception.user.LoanApplicationException;
import com.nefu.project.common.result.HttpResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class LoanApplicationHander {
    /**
     * 融资模块的错误
     */
    @ExceptionHandler(LoanApplicationException.class)
    public HttpResult<String> loanApplicationExceptionHandler(LoanApplicationException e){
        return HttpResult.failed(e.getMessage());
    }
}
