package com.xian.eduucenter.entities.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录对象",description = "登录对象")
public class LoginVo {
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("密码")
    private String code;
}
