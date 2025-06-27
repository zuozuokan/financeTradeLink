package com.nefu.project.common.handler;

import com.nefu.project.common.exception.productManager.UploadException;
import com.nefu.project.common.result.HttpResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UploadExceptionHandler {
    @ExceptionHandler(UploadException.class)
    public HttpResult<String> uploadException(UploadException e){
        return HttpResult.failed("商品图片上传失败");
    }
}
