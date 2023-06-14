package com.xian.aclservice.entities;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@Data
@TableName("acl_permission")
@ApiModel(value = "Permission对象", description = "权限")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
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
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
