package com.xian.eduservice.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xian.eduservice.entities.Course;
import com.xian.eduservice.service.CourseService;
import com.xian.utils.ConstantUtil;
import com.xian.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
public class CourseTasks {
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private CourseService courseService;
    /**
     * 定时更新问题浏览量到数据库中
     * 每天凌晨两点跑一次
     */
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional(rollbackFor = Exception.class)
    public void updateQuestionView() {
        log.info("start to update course view count...");
        Set<ZSetOperations.TypedTuple<String>> courseViewSet = redisUtil.zRangeWithScores(ConstantUtil.COURSE_VIEW_KEY, 0, -1);
        courseViewSet.forEach(courseView -> {
            LambdaUpdateWrapper<Course> courseUpdateWrapper = new LambdaUpdateWrapper<>();
            courseUpdateWrapper.eq(Course::getId,courseView.getValue()).setSql("view_count = view_count + " + courseView.getScore());
            courseService.update(courseUpdateWrapper);
        });
        redisUtil.delete(ConstantUtil.COURSE_VIEW_KEY);
        log.info("update course view count finish");
        log.info("start to update course buy count...");
        Set<ZSetOperations.TypedTuple<String>> courseBuySet = redisUtil.zRangeWithScores(ConstantUtil.COURSE_BUY_KEY, 0, -1);
        courseBuySet.forEach(courseBuy -> {
            LambdaUpdateWrapper<Course> courseUpdateWrapper = new LambdaUpdateWrapper<>();
            courseUpdateWrapper.eq(Course::getId,courseBuy.getValue()).setSql("buy_count = buy_count + " + courseBuy.getScore());
            courseService.update(courseUpdateWrapper);
        });
        redisUtil.delete(ConstantUtil.COURSE_BUY_KEY);
        log.info("update course buy count finish");
    }
}
