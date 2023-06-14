package com.xian.edusms.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MoblieCode {
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("验证码")
    private String code;
}
