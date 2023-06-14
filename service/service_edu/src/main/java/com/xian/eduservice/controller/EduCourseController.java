package com.xian.eduservice.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.eduservice.annotations.CoursePublish;
import com.xian.eduservice.annotations.CourseView;
import com.xian.eduservice.entities.Course;
import com.xian.eduservice.entities.vo.CourseInfo;
import com.xian.eduservice.entities.vo.CourseQueryVo;
import com.xian.eduservice.handler.DefaultBlockHandler;
import com.xian.eduservice.service.CourseService;
import com.xian.entities.R;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:47
 */
@CrossOrigin
@RestController
@RequestMapping("/edu/course")
@SentinelResource(value = "DefaultBlockHandler", blockHandlerClass = DefaultBlockHandler.class, blockHandler = "defaultHandlerException")
public class EduCourseController {
    @Autowired
    private CourseService courseService;
    @PostMapping("/addCourseInfo")
    @CoursePublish
    public R addCourseInfo(@RequestBody CourseInfo courseInfo) {
        return courseService.addCourseInfo(courseInfo);
    }
    @GetMapping("/getCourseInfoById/{courseId}")
    public R getCourseInfoById(@PathVariable("courseId") String courseId) {
        CourseInfo courseInfo = courseService.getCourseInfoById(courseId);
        return R.ok().data("courseInfo", courseInfo);
    }
    @PostMapping("/getCourseInfoByBatchId")
    public R getCourseInfoByBatchId(@RequestBody List<String> courseIdList) {
        List<CourseInfo> courseInfoList = courseService.getCourseInfoByBatchId(courseIdList);
        return R.ok().data("courseInfo", courseInfoList);
    }
    @PostMapping("/modifyCourseInfo")
    public R modifyCourseInfo(@RequestBody CourseInfo courseInfo) {
        return R.returnR(courseService.modifyCourseInfo(courseInfo));
    }
    @GetMapping
    public R getListCourse() {
        return R.ok().data("list",courseService.list());
    }

    @ApiOperation("selectCourseByCondition")
    @PostMapping({"/search/{current}/{count}"})
    public R selectCourseByCondition(@PathVariable(value = "current")Integer current, @PathVariable(value = "count",required = false)Integer count, @RequestBody(required = false) CourseQueryVo courseQuery) {        //创建page对象
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if (courseQuery!=null) {
            String title = courseQuery.getTitle();
            String status = courseQuery.getStatus();
            String teacherId = courseQuery.getTeacherId();
            Boolean buyCountSort = courseQuery.getBuyCountSort();
            Boolean priceSort = courseQuery.getPriceSort();
            String subjectId = courseQuery.getSubjectId();
            String subjectParentId = courseQuery.getSubjectParentId();
            Boolean gmtCreateSort = courseQuery.getGmtCreateSort();
            if (!ObjectUtils.isEmpty(title)) {
                wrapper.like("title",title);
            } if (StringUtils.hasText(status)) {
                wrapper.eq("status",status);
            } if (!ObjectUtils.isEmpty(teacherId)) {
                wrapper.eq("teacher_id",teacherId);
            } if (!ObjectUtils.isEmpty(subjectId)) {
                wrapper.eq("subject_id",subjectId);
            } if (!ObjectUtils.isEmpty(subjectParentId)) {
                wrapper.eq("subject_parent_id",subjectParentId);
            } if (!ObjectUtils.isEmpty(buyCountSort) && buyCountSort) {
                wrapper.orderByDesc("buy_count");
            } else if (!ObjectUtils.isEmpty(priceSort) && priceSort) {
                wrapper.orderByDesc("price");
            } else if (!ObjectUtils.isEmpty(gmtCreateSort) && gmtCreateSort) {
                wrapper.orderByDesc("gmt_create");
            } else {
                wrapper.orderByDesc("gmt_create");
            }
        }
        return pageList(current,count,wrapper);
    }
    @DeleteMapping("/delCourseById/{courseId}")
    public R delCourseById(@PathVariable String courseId) {
        return courseService.delCourseById(courseId);
    }

    private R pageList(Integer current, Integer count, QueryWrapper<Course> wrapper) {
        Page<Course> page = new Page<>(current,count);
        courseService.page(page,wrapper);
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("list",page.getRecords());
    }

//    @GetMapping("/updateCourseBuyCount/{courseId}")
//    public R updateCourseBuyCount(@PathVariable("courseId")String courseId) {
//        LambdaUpdateWrapper<Course> courseUpdateWrapper = new LambdaUpdateWrapper<>();
//        courseUpdateWrapper.eq(Course::getId,courseId).setSql("buy_count = buy_count + 1");
//        if (courseService.update(courseUpdateWrapper))
//            return R.ok();
//        else
//            return R.fail();
//    }

}
