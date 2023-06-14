package com.xian.eduservice.entities.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "条件查询对象",description = "讲师查询对象")
@Data
public class TeacherQuery implements Serializable {
    private static final Long serialVersionID=1L;

    @ApiModelProperty("教师名称，模糊查询")
    private String name;
    @ApiModelProperty("教师等级，头衔")
    private Integer level;
    @ApiModelProperty("查询开始时间")
    private String begin;
    @ApiModelProperty("查询结束时间")
    private String end;
}
