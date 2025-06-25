package com.nefu.project.common.exception.productManager;

import com.nefu.project.common.result.HttpResult;

public class ProductAddException extends RuntimeException {
    public ProductAddException(String message){
        super(message);
    }
}
