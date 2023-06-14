package com.xian.eduservice.aop;

import com.xian.utils.ConstantUtil;
import com.xian.entities.R;
import com.xian.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class CourseAspect {
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 定义课程浏览量切点
     */
    @Pointcut("@annotation(com.xian.eduservice.annotations.CourseView)")
    public void courseViewPointCut(){}

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.xian.eduservice.annotations.CoursePublish)")
    public void coursePublishPointCut(){}

    /**
     * 切入处理，后置通知
     * @param point
     * @return
     */
    @After("courseViewPointCut()")
    public void afterMethod(JoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        String courseId = (String) args[0];
        redisUtil.zIncrementScore(ConstantUtil.COURSE_VIEW_KEY, courseId, 1);
    }

    /**
     * 切入处理，环绕通知
     * @param point
     * @return
     */
    @Around("coursePublishPointCut()")
    public Object afterMethod(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();
        try {
            R result = (R) proceed;
            if (result.getSuccess()) {
                redisUtil.pfAdd(ConstantUtil.COURSE_PUBLISH_KEY, (String) result.getData().get("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return proceed;
        }
        return proceed;
    }
}
