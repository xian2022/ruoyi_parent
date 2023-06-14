package com.xian.eduservice.entities;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 讲师
 * </p>
 *
 * @author xian
 * @since 2022/04/19 20:57
 */
@Getter
@Setter
@TableName("edu_teacher")
@ApiModel(value = "Teacher对象", description = "讲师")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("讲师ID")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("讲师姓名")
    private String name;

    @ApiModelProperty("讲师简介")
    private String intro;

    @ApiModelProperty("讲师资历,一句话说明讲师")
    private String career;

    @ApiModelProperty("头衔 1高级讲师 2首席讲师")
    private Integer level;

    @ApiModelProperty("讲师头像")
    private String avatar;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic("0")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间",dataType = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间",dataType = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}
