package com.nefu.project.common.result;

public enum RespCode {
    SUCCESS(200, "success"),
    FAILED(500, "failed");

    final int code;
    final String msg;

//    构造函数
    RespCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
