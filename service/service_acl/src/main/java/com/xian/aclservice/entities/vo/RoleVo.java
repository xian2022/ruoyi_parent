package com.xian.aclservice.entities.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoleVo {
    @ApiModelProperty("角色id")
    private String id;

    @ApiModelProperty("角色名称")
    private String roleName;
}
