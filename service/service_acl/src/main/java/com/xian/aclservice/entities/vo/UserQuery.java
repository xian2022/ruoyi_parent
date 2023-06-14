package com.xian.aclservice.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserQuery {
    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("用户名")
    private String username;
}
