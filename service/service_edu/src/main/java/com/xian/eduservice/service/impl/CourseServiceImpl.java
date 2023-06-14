package com.xian.eduservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduservice.entities.Chapter;
import com.xian.eduservice.entities.Course;
import com.xian.eduservice.entities.CourseDescription;
import com.xian.eduservice.entities.Video;
import com.xian.eduservice.entities.vo.CourseInfo;
import com.xian.eduservice.fegin.VodFegin;
import com.xian.eduservice.mapper.CourseMapper;
import com.xian.eduservice.service.ChapterService;
import com.xian.eduservice.service.CourseDescriptionService;
import com.xian.eduservice.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.eduservice.service.VideoService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:47
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    private CourseDescriptionService courseDescriptionService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private VodFegin vodFegin;
    @Transactional
    @Override
    public R addCourseInfo(CourseInfo courseInfo) {
        // 将courseInfo转换成course对象
        Course course = new Course();
        BeanUtil.copyProperties(courseInfo,course);
        if (BeanUtil.isEmpty(course)) {
            return R.fail();
        }
        if (this.save(course)) {
            courseInfo.setId(course.getId());
        } else {
            return R.fail();
        }
        CourseDescription description = new CourseDescription();
        BeanUtil.copyProperties(courseInfo,description);
        if (BeanUtil.isEmpty(description)) {
            return R.fail();
        }
        return R.returnR(courseDescriptionService.save(description)).data("id",courseInfo.getId());
    }

    @Override
    public CourseInfo getCourseInfoById(String courseId) {
        Course course = getOne(new QueryWrapper<Course>().eq("id", courseId));
        CourseInfo courseInfo = BeanUtil.copyProperties(course, CourseInfo.class);
        CourseDescription description = courseDescriptionService.getOne(new QueryWrapper<CourseDescription>().eq("id", courseId));
        if (ObjectUtil.isNull(description)) {
            courseInfo.setDescription(null);
        } else {
            courseInfo.setDescription(description.getDescription());
        }
        return courseInfo;
    }

    @Transactional
    @Override
    public boolean modifyCourseInfo(CourseInfo courseInfo) {
        Course course = BeanUtil.copyProperties(courseInfo, Course.class);
        CourseDescription description = BeanUtil.copyProperties(courseInfo, CourseDescription.class);
        return updateById(course) && courseDescriptionService.updateById(description);
    }

    @Transactional
    @Override
    public R delCourseById(String courseId) {
        List<Chapter> chapterLists = chapterService.list(new QueryWrapper<Chapter>().eq("course_id",courseId));
        List<String> videoIdList = new ArrayList<>();
        try {
            chapterLists.forEach(chapter -> {
                List<Video> videoList = videoService.list(new QueryWrapper<Video>().eq("chapter_id", chapter.getId()).eq("course_id", courseId));
                videoList.forEach(video -> videoIdList.add(video.getVideoSourceId()));
                videoService.removeBatchByIds(videoList);
            });
            if (videoIdList.size()>0) {
                vodFegin.delAliyunVideo(videoIdList);
            }
            chapterService.removeBatchByIds(chapterLists);
            removeById(courseId);
        } catch (Exception e) {
            return R.fail().message("出错啦！删除课程失败！");
        }
        return R.ok().message("删除课程成功！");
    }

    @Override
    public R selectByTeacherId(String teacherId) {
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("teacher_id",teacherId).orderByDesc("gmt_modified");
        List<Course> list = list(courseQueryWrapper);
        return R.ok().data("items",list);
    }

    @Override
    public List<CourseInfo> getCourseInfoByBatchId(List<String> courseIdList) {
        List<Course> courseList = listByIds(courseIdList);
        List<CourseInfo> courseInfoList = BeanUtil.copyToList(courseList, CourseInfo.class);
        List<CourseDescription> descriptionList = courseDescriptionService.listByIds(courseIdList);
        descriptionList.forEach(desc -> {
            String id = desc.getId();
            courseInfoList.forEach(courseInfo -> {
                if (courseInfo.getId().equals(id)) {
                    courseInfo.setDescription(desc.getDescription());
                }
            });
        });
        return courseInfoList;
    }

    @Override
    public R selectBySubjectId(String subjectId) {
        if (StringUtils.hasText(subjectId)) {
            QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
            courseQueryWrapper.eq("subject_parent_id",subjectId).orderByDesc("view_count").last("limit 4");
            List<Course> list = list(courseQueryWrapper);
            return R.ok().data("items",list);
        }
        return R.fail().message("输入参数非法！");
    }
}
