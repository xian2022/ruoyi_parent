package com.xian.eduservice.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduservice.entities.Course;
import com.xian.eduservice.entities.Teacher;
import com.xian.eduservice.entities.subject.OneSubject;
import com.xian.eduservice.entities.subject.TwoSubject;
import com.xian.eduservice.service.CourseService;
import com.xian.eduservice.service.SubjectService;
import com.xian.eduservice.service.TeacherService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 课程 前台数据显示
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:47
 */
@CrossOrigin
@RestController
@RequestMapping("/edu/indexfront")
public class IndexFrontController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private TeacherService teacherService;
    @GetMapping("/index/{count}")
    public R index(@PathVariable("count") Integer count) {
        if (Objects.isNull(count)) {
            count = 3;
        }
        List<Course> hotCourseList = getHotCourses();
        List<TwoSubject> courseList = getCourseList(count);
        return R.ok().data("hotCourse",hotCourseList).data("courseList",courseList);
    }

    @GetMapping("/indexAndTeacher/{courseCount}/{teacherCount}")
    public R indexAndHotTeacher(@PathVariable("courseCount") Integer courseCount,@PathVariable("teacherCount") Integer teacherCount) {
        if (Objects.isNull(courseCount)) {
            courseCount = 3;
        }
        if (Objects.isNull(teacherCount)) {
            teacherCount = 8;
        }
        List<Course> hotCourseList = getHotCourses();
        List<TwoSubject> courseList = getCourseList(courseCount);
        List<Teacher> getHotTeacherList = getHotTeachers(teacherCount);
        return R.ok().data("hotCourse",hotCourseList).data("courseList",courseList);
    }

    @Cacheable(value = "hotTeachers", key ="'getHotTeachers'")
    public List<Teacher> getHotTeachers(Integer count) {
        // 查询前8条名师
        QueryWrapper<Teacher> hotTeacherWrapper = new QueryWrapper<Teacher>().orderByDesc("sort").last("limit 0,"+count);
        return teacherService.list(hotTeacherWrapper);
    }

    @Cacheable(value = "hotCourses", key ="'getHotCourses'")
    public List<Course> getHotCourses() {
        // 查询前8条热门课程
        QueryWrapper<Course> hotCourseWrapper = new QueryWrapper<Course>().eq("status","Normal").orderByDesc("view_count").last("limit 0,8");
        return courseService.list(hotCourseWrapper);
    }

    @Cacheable(value = "courseList3", key ="'getCourseList'")
    public List<TwoSubject> getCourseList(Integer count) {
        return subjectService.getSubjectCourseByCount(count);
    }
}
