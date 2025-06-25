package com.nefu.project.common.handler;

import com.nefu.project.common.exception.productManager.ProductManagerException;
import com.nefu.project.common.result.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ProductManagerHandler {
    @ExceptionHandler(ProductManagerException.class)
    public HttpResult<String> productManagerException(ProductManagerException e){
        log.debug( e.getMessage());
        return HttpResult.failed(e.getMessage());
    }

}
