package com.xian.eduservice.entities;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 课程
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:52
 */
@Getter
@Setter
@TableName("edu_chapter")
@ApiModel(value = "Chapter对象", description = "课程")
public class Chapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("章节ID")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("课程ID")
    @TableField("course_id")
    private String courseId;

    @ApiModelProperty("章节名称")
    @TableField("title")
    private String title;

    @ApiModelProperty("显示排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
