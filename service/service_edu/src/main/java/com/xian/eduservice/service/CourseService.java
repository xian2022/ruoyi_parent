package com.xian.eduservice.service;

import com.xian.eduservice.entities.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.eduservice.entities.vo.CourseInfo;
import com.xian.entities.R;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:47
 */
public interface CourseService extends IService<Course> {

    R addCourseInfo(CourseInfo courseInfo);

    CourseInfo getCourseInfoById(String courseId);

    boolean modifyCourseInfo(CourseInfo courseInfo);

    R delCourseById(String courseId);

    R selectByTeacherId(String teacherId);

    List<CourseInfo> getCourseInfoByBatchId(List<String> courseIdList);

    R selectBySubjectId(String subjectId);
}
