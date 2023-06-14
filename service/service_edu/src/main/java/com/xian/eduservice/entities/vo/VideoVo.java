package com.xian.eduservice.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VideoVo {
    private String id;
    private Integer sort;
    private String chapterId;
    private String courseId;
    private String videoSourceId;
    private String videoOriginalName;
    private Integer isFree;
    private String title;
}
