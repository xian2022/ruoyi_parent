package com.xian.eduservice.entities;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author xian
 * @since 2022/06/20 08:36
 */
@Getter
@Setter
@TableName("edu_comment")
@ApiModel(value = "Comment对象", description = "评论")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论ID")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("课程id")
    @TableField("course_id")
    private String courseId;

    @ApiModelProperty("讲师id")
    @TableField("teacher_id")
    private String teacherId;

    @ApiModelProperty("会员id")
    @TableField("member_id")
    private String memberId;

    @ApiModelProperty("会员昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty("会员头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty("评论内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic("0")
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
