package com.xian.eduservice.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseInfo {
    @ApiModelProperty("课程ID")
    @TableId("id")
    private String id;

    @ApiModelProperty("课程讲师ID")
    @TableField("teacher_id")
    private String teacherId;

    @ApiModelProperty("课程专业ID")
    @TableField("subject_id")
    private String subjectId;

    @ApiModelProperty("课程专业父级ID")
    @TableField("subject_parent_id")
    private String subjectParentId;

    @ApiModelProperty("课程标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("课程销售价格，设置为0则可免费观看")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty("总课时")
    @TableField("lesson_num")
    private Integer lessonNum;

    @ApiModelProperty("课程状态 Draft未发布  Normal已发布")
    @TableField("status")
    private String status;

    @ApiModelProperty("课程封面图片路径")
    @TableField("cover")
    private String cover;

    @ApiModelProperty("课程简介")
    @TableField("description")
    private String description;
}
