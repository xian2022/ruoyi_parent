package com.xian.aclservice.entities.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserLogin {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户名")
    private String password;
}
