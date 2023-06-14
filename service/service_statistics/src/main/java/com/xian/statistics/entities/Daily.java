package com.xian.statistics.entities;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 网站统计日数据
 * </p>
 *
 * @author xian
 * @since 2022/06/28 23:35
 */
@Getter
@Setter
@TableName("statistics_daily")
@ApiModel(value = "Daily对象", description = "网站统计日数据")
public class Daily implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("统计日期")
    @TableField("date_calculated")
    private String dateCalculated;

    @ApiModelProperty("注册人数")
    @TableField("register_num")
    private Integer registerNum;

    @ApiModelProperty("登录人数")
    @TableField("login_num")
    private Integer loginNum;

    @ApiModelProperty("每日播放视频数")
    @TableField("video_view_num")
    private Integer videoViewNum;

    @ApiModelProperty("每日新增课程数")
    @TableField("course_num")
    private Integer courseNum;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
