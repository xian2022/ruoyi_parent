package com.xian.entities;

public enum ResultCode {
    SUCCESS(20000),
    EXCEPTION(400),
    ERROR(20001);

    public Integer code;
    ResultCode(Integer code){
        this.code = code;
    }
}
