package com.xian.eduservice.controller.front;

import com.xian.eduservice.annotations.CourseView;
import com.xian.eduservice.controller.EduChapterController;
import com.xian.eduservice.controller.EduCourseController;
import com.xian.eduservice.controller.EduSubjectController;
import com.xian.eduservice.entities.Course;
import com.xian.eduservice.entities.vo.CourseQueryVo;
import com.xian.eduservice.service.CourseService;
import com.xian.eduservice.service.TeacherService;
import com.xian.entities.R;
import com.xian.servicebase.handler.RuoyiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/edu/coursefront")
public class CourseFrontController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private EduCourseController courseController;
    @Autowired
    private EduSubjectController subjectController;
    @Autowired
    private EduChapterController chapterController;

    @ApiOperation(value = "分页课程列表")
    @PostMapping(value = "/{current}/{count}")
    public R getCourseFrontList(@PathVariable("current")Integer current, @PathVariable("count")Integer count, @RequestBody(required = false) CourseQueryVo courseQueryVo) {
        return courseController.selectCourseByCondition(current,count,courseQueryVo);
    }

    @ApiOperation(value = "课程分类")
    @GetMapping(value = "/getCourseSubject")
    public R getCourseSubject() {
        return subjectController.getAllSubject();
    }

    @ApiOperation(value = "根据课程id查询讲师id")
    @GetMapping(value = "getTeacherInfoById/{courseId}")
    public R getTeacherInfoByCourseId(@PathVariable("courseId")String courseId) {
        return teacherService.getTeacherInfoByCourseId(courseId);
    }

    @ApiOperation(value = "根据课程id查询章节")
    @GetMapping(value = "getChapterById/{courseId}")
    public R getChapterById(@PathVariable("courseId")String courseId) {
        return chapterController.getChapterVideo(courseId);
    }

    @ApiOperation(value = "根据课程id查询课程")
    @GetMapping(value = "courseInfo/{courseId}")
    public R getCourseInfoById(@PathVariable("courseId")String courseId) {
        return courseController.getCourseInfoById(courseId);
    }
    @ApiOperation(value = "根据课程id查询课程")
    @GetMapping(value = "course/{courseId}")
    @CourseView
    public R getCourseById(@PathVariable("courseId")String courseId) {
        Course course;
        try {
            course = courseService.getById(courseId);
        } catch (Exception e) {
            throw new RuoyiException(20001,"课程不存在！");
        }
        return R.ok().data("items",course);
    }
    @ApiOperation(value = "根据科目id查询课程列表")
    @GetMapping(value = "getCourseBySubjectParentId/{subjectParentId}")
    @CourseView
    public R getCourseBySubjectParentId(@PathVariable("subjectParentId")String subjectParentId) {
        return courseService.selectBySubjectId(subjectParentId);
    }
}
