package com.xian.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R {
    @ApiModelProperty("是否成功")
    private Boolean success;
    @ApiModelProperty("响应码")
    private Integer code;
    @ApiModelProperty("返回消息")
    private String message;
    @ApiModelProperty("返回数据")
    private Map<String,Object> data = new HashMap<>();
    private R(){}
    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS.code);
        return r;
    }
    public static R returnR(boolean flag){
        return flag?ok():fail();
    }
    public static R fail(){
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.EXCEPTION.code);
        return r;
    }

    public R success(Boolean success) {
        this.success = success;
        return this;
    }

    public R code(Integer code) {
        this.code = code;
        return this;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    public R data(String key,Object value) {
        data.put(key,value);
        return this;
    }
    public R data(Map<String, Object> map) {
        data = map;
        return this;
    }
}
