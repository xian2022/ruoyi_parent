package com.xian.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduservice.entities.Course;
import com.xian.eduservice.entities.Teacher;
import com.xian.eduservice.entities.vo.TeacherInfo;
import com.xian.eduservice.mapper.TeacherMapper;
import com.xian.eduservice.service.CourseService;
import com.xian.eduservice.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/04/19 20:57
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private CourseService courseService;

    @Override
    public R getTeacherById(String id) {
        return R.ok().data("items",getById(id));
    }

    @Override
    public R getTeacherInfoByCourseId(String courseId) {
        Course course = courseService.getById(courseId);
        String teacherId = course.getTeacherId();
        return getTeacherById(teacherId);
    }
}
