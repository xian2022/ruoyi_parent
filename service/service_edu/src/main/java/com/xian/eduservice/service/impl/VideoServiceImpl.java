package com.xian.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduservice.entities.Course;
import com.xian.eduservice.entities.Video;
import com.xian.eduservice.mapper.CourseMapper;
import com.xian.eduservice.mapper.TeacherMapper;
import com.xian.eduservice.mapper.VideoMapper;
import com.xian.eduservice.service.CourseDescriptionService;
import com.xian.eduservice.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:52
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private CourseDescriptionService courseDescriptionService;
    @Override
    public R getVideoList(String videoId,Boolean isBuy) {
        Video video = getById(videoId);
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",video.getCourseId());
        if (!isBuy) {
            videoQueryWrapper.eq("is_free",0);
        }
        return R.ok().data("items",list(videoQueryWrapper));
    }

    @Override
    public R getCourse(String videoId) {
        Video video = getById(videoId);
        String courseId = video.getCourseId();
        return R.ok().data("items",courseMapper.selectById(courseId)).data("desc",courseDescriptionService.getById(courseId));
    }

    @Override
    public R getTeacher(String videoId) {
        Video video = getById(videoId);
        String courseId = video.getCourseId();
        Course course = courseMapper.selectById(courseId);
        String teacherId = course.getTeacherId();
        return R.ok().data("items",teacherMapper.selectById(teacherId));
    }

    @Override
    public String getResourceId(String videoId) {
        Video video = getById(videoId);
        return video.getVideoSourceId();
    }
}
