package com.xian.eduservice.entities.vo;

import com.xian.eduservice.entities.Video;
import lombok.Data;

import java.util.List;

@Data
public class ChapterVo {
    private String id;
    private String courseId;
    private Integer sort;
    private String title;
    private List<VideoVo> children;

    @Override
    public String toString() {
        return "ChapterVo{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", sort=" + sort +
                ", title='" + title + '\'' +
                ", children=" + children +
                '}';
    }
}
