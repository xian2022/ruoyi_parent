package com.xian.aclservice.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class PermissionVo {
    @ApiModelProperty("编号")
    @TableId("id")
    private String id;

    @ApiModelProperty("所属上级")
    @TableField("pid")
    private String pid;

    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("类型(1:菜单,2:按钮)")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("权限值")
    @TableField("permission_value")
    private String permissionValue;

    @ApiModelProperty("访问路径")
    @TableField("path")
    private String path;

    @ApiModelProperty("组件路径")
    @TableField("component")
    private String component;

    @ApiModelProperty("图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty("状态(0:禁止,1:正常)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField("is_deleted")
    private Integer isDeleted;

    @ApiModelProperty("子权限")
    @TableField("children")
    private List<PermissionVo> children;
}
