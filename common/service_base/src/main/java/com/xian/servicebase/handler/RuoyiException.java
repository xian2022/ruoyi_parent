package com.xian.servicebase.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RuoyiException extends RuntimeException {
    private Integer code;//状态码
    private String msg;//异常信息
    public RuoyiException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
