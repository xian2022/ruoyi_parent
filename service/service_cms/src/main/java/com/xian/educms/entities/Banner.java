package com.xian.educms.entities;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 首页banner表
 * </p>
 *
 * @author xian
 * @since 2022/05/27 16:31
 */
@Getter
@Setter
@TableName("crm_banner")
@ApiModel(value = "Banner对象", description = "首页banner表")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("图片地址")
    @TableField("image_url")
    private String imageUrl;

    @ApiModelProperty("链接地址")
    @TableField("link_url")
    private String linkUrl;

    @ApiModelProperty("排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
