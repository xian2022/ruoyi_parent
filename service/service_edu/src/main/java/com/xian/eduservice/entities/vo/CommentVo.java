package com.xian.eduservice.entities.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CommentVo {
    @ApiModelProperty("评论ID")
    private String id;

    @ApiModelProperty("课程id")
    private String courseId;

    @ApiModelProperty("讲师Id")
    private String teacherId;

    @ApiModelProperty("会员昵称")
    private String nickname;

    @ApiModelProperty("会员id")
    @TableField("member_id")
    private String memberId;

    @ApiModelProperty("会员头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty("评论内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;
}
