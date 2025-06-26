package com.nefu.project.common.exception.gateway;

public class TokenPermissionException extends RuntimeException {
    public TokenPermissionException(String message) {
        super(message);
    }
}
