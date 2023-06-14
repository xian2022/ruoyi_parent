package com.xian.aclservice.entities.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RolePermissionVo {
    @ApiModelProperty("角色id")
    private String id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色权限")
    private List<String> permissionIds;
}
