package com.xian.aop;

import com.xian.entities.R;
import com.xian.utils.ConstantUtil;
import com.xian.utils.JwtUtil;
import com.xian.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@Component
public class MemberPermissionAop {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 定义检查登录切点
     */
    @Pointcut("@annotation(com.xian.annotation.CheckMemberLogin)")
    public void checkMemberLoginPointCut(){}

    @Around("checkMemberLoginPointCut()")
    public Object myBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String id = JwtUtil.getIdByJwtToken(request);
            String key = ConstantUtil.LOGIN_USER_KEY + id;
            String s = redisUtil.get(key);
            if (!StringUtils.hasText(s)) {
                throw new NullPointerException();
            } else {
                redisUtil.expire(key,30L, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            return R.fail().message("登录已过期！").code(50014);
        }
        return joinPoint.proceed();
    }
}
