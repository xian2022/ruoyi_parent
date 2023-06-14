package com.xian.aclservice.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoleQuery {
    @ApiModelProperty("角色名称")
    @TableField("role_name")
    private String roleName;
}
