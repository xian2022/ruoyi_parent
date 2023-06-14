package com.xian.eduservice.service;

import com.xian.eduservice.entities.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.eduservice.entities.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:52
 */
public interface ChapterService extends IService<Chapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    boolean deleteChapterById(String chapterId);

    boolean saveList(List<ChapterVo> chapterList);
}
