package com.xian.eduservice.aop;

import com.xian.utils.ConstantUtil;
import com.xian.eduservice.utils.IpUtils;
import com.xian.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Slf4j
@Component
public class VideoAspect {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.xian.eduservice.annotations.VideoPlay)")
    public void videoPlayPointCut(){}

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.xian.eduservice.annotations.VideoView)")
    public void videoViewPointCut(){}

    /**
     * 切入处理，后置通知
     * @param point
     * @return
     */
    @After("videoPlayPointCut()")
    public void afterPlayMethod(JoinPoint point) {
        Object[] args = point.getArgs();
        String videoId = (String) args[0];
        redisUtil.zIncrementScore(ConstantUtil.VIDEO_PLAY_KEY, videoId, 1);
    }

    /**
     * 切入处理，后置通知
     */
    @After("videoViewPointCut()")
    public void afterViewMethod() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            redisUtil.pfAdd(ConstantUtil.VIDEO_VIEW_KEY, IpUtils.getIpAddr(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
