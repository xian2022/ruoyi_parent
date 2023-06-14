package com.xian.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduservice.controller.EduCourseController;
import com.xian.eduservice.controller.EduTeacherController;
import com.xian.eduservice.entities.Teacher;
import com.xian.eduservice.entities.vo.CourseQueryVo;
import com.xian.eduservice.entities.vo.TeacherQuery;
import com.xian.eduservice.service.CourseService;
import com.xian.eduservice.service.TeacherService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 讲师 前台数据显示
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:47
 */
@CrossOrigin
@RestController
@RequestMapping("/edu/teacherfront")
public class TeacherFrontController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private EduCourseController courseController;
    @Autowired
    private EduTeacherController teacherController;
    @PostMapping("/getTeacherFrontList/{current}")
    public R getTeacherFrontList(@PathVariable(value = "current")Integer current, @RequestBody(required = false) TeacherQuery teacherQuery) {
        return teacherController.selectTeacherByCondition(current,8,teacherQuery);
    }
    @GetMapping("/{teacherId}")
    public R getTeacherInfoById(@PathVariable(value = "teacherId")String teacherId) {
        return teacherController.getTeacherById(teacherId);
    }
    /**
     * 根据讲师id查询当前讲师的课程列表
     * @param teacherId
     * @return
     */
    @GetMapping("/course/{teacherId}/{current}")
    public R selectByTeacherId(@PathVariable("teacherId") String teacherId,@PathVariable("current") Integer current) {
        CourseQueryVo courseQueryVo = new CourseQueryVo();
        courseQueryVo.setTeacherId(teacherId);
        return courseController.selectCourseByCondition(current,8,courseQueryVo);
    }
}
