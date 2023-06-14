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
 * 课程视频
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:52
 */
@Getter
@Setter
@TableName("edu_video")
@ApiModel(value = "Video对象", description = "课程视频")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("视频ID")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("课程ID")
    @TableField("course_id")
    private String courseId;

    @ApiModelProperty("章节ID")
    @TableField("chapter_id")
    private String chapterId;

    @ApiModelProperty("节点名称")
    @TableField("title")
    private String title;

    @ApiModelProperty("云端视频资源")
    @TableField("video_source_id")
    private String videoSourceId;

    @ApiModelProperty("原始文件名称")
    @TableField("video_original_name")
    private String videoOriginalName;

    @ApiModelProperty("排序字段")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("播放次数")
    @TableField("play_count")
    private Long playCount;

    @ApiModelProperty("是否可以试听：0收费 1免费")
    @TableField("is_free")
    private Integer isFree;

    @ApiModelProperty("视频时长（秒）")
    @TableField("duration")
    private Float duration;

    @ApiModelProperty("Empty未上传 Transcoding转码中  Normal正常")
    @TableField("status")
    private String status;

    @ApiModelProperty("视频源文件大小（字节）")
    @TableField("size")
    private Long size;

    @ApiModelProperty("乐观锁")
    @TableField("version")
    @Version
    private Long version;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
