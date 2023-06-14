package com.xian.eduservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduservice.entities.Chapter;
import com.xian.eduservice.entities.Video;
import com.xian.eduservice.entities.vo.ChapterVo;
import com.xian.eduservice.entities.vo.VideoVo;
import com.xian.eduservice.mapper.ChapterMapper;
import com.xian.eduservice.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.eduservice.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:52
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {
    @Autowired
    private VideoService videoService;
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        List<Chapter> chapterList = this.list(new QueryWrapper<Chapter>().eq("course_id", courseId));
        List<ChapterVo> chapterVoList = new ArrayList<>();
        chapterList.forEach(chapter -> {
            ChapterVo chapterVo = BeanUtil.copyProperties(chapter, ChapterVo.class);
            List<VideoVo> videoList = new ArrayList<>();
            videoService.list(new QueryWrapper<Video>().eq("course_id", courseId).eq("chapter_id", chapter.getId())).forEach(video -> {
                VideoVo videoVo = BeanUtil.copyProperties(video, VideoVo.class);
                videoList.add(videoVo);
            });
            chapterVo.setChildren(videoList);
            chapterVoList.add(chapterVo);
        });
        return chapterVoList;
    }

    @Override
    public boolean deleteChapterById(String chapterId) {
        List<Video> videoListByChapterId = videoService.list(new QueryWrapper<Video>().eq("chapter_id", chapterId));
        videoListByChapterId.forEach(video -> videoService.removeById(video.getId()));
        return removeById(chapterId);
    }

    @Override
    public boolean saveList(List<ChapterVo> chapterList) {
        try {
            chapterList.forEach(chapterVo -> {
                Chapter chapter = BeanUtil.copyProperties(chapterVo, Chapter.class);
                chapter.setId(null);
                save(chapter);
                List<Video> videoList = BeanUtil.copyToList(chapterVo.getChildren(), Video.class);
                videoList.forEach(video -> {
                    video.setCourseId(chapter.getCourseId());
                    video.setChapterId(chapter.getId());
                });
                videoService.saveBatch(videoList);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
