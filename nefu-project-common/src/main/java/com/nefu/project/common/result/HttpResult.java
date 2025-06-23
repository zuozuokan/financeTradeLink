package com.nefu.project.common.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/*
 * 设置泛型<T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResult<T> {

    @Schema(description = "返回代码,200-成功,500-失败")
    private int code; // 返回代码 200，500

    @Schema(description = "返回信息")
    private String msg; // 返回代码描述

    @Schema(description = "返回结果")
    private T results; // 返回结果

    @Schema(description = "返回时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time; // 返回时间

    /*
    * 调用成功返回结果
    * */
    public static <T> HttpResult<T> success(T results) {
        return new HttpResult<T>(
                RespCode.SUCCESS.code,
                RespCode.SUCCESS.msg,
                results,
                new Date());
    }

//    失败返回信息
    public static <T> HttpResult<T> failed(T results) {
        return new HttpResult<T>(
                RespCode.FAILED.code,
                RespCode.FAILED.msg,
                results,
                new Date());
    }

//     失败不返回信息
    public static <T> HttpResult<T> failed() {
       return failed(null);
    }
}
