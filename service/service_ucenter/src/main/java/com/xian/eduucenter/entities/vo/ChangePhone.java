package com.xian.eduucenter.entities.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChangePhone {
    @ApiModelProperty("会员id")
    private String id;

    @ApiModelProperty("旧手机号")
    private String oldMobile;

    @ApiModelProperty("新手机号")
    private String mobile;

    @ApiModelProperty("验证码")
    private String code;
}
