package com.xian.edusms.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Result {
    @ApiModelProperty("信息")
    private String msg;
    @ApiModelProperty("批次号")
    private String smsid;
    @ApiModelProperty("错误码")
    private Integer code;
    @ApiModelProperty("不合法参数")
    private List<String> ILLEGAL_WORDS;

    private Result() {}
    private Result(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public static Result success() {
       return new Result("成功！",0);
    }
}
