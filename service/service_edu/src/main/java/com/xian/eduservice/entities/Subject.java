package com.xian.eduservice.entities;

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
 * 课程科目
 * </p>
 *
 * @author xian
 * @since 2022/05/14 09:52
 */
@Getter
@Setter
@TableName("edu_subject")
@ApiModel(value = "Subject对象", description = "课程科目")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程类别ID")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("类别名称")
    private String title;

    @ApiModelProperty("父ID")
    private String parentId;

    @ApiModelProperty("排序字段")
    private Integer sort;

    @ApiModelProperty(value = "创建时间",dataType = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间",dataType = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
