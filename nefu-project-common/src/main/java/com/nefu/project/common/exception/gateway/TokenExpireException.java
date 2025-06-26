package com.nefu.project.common.exception.gateway;

public class TokenExpireException extends RuntimeException {
    public TokenExpireException(String message) {
        super(message);
    }
}
