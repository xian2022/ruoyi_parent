package com.xian.eduucenter.aop;

import com.xian.eduucenter.entities.Member;
import com.xian.eduucenter.entities.vo.LoginVo;
import com.xian.eduucenter.entities.vo.RegisterVo;
import com.xian.utils.ConstantUtil;
import com.xian.entities.R;
import com.xian.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class MemberAspect {
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 定义登录切点
     */
    @Pointcut("@annotation(com.xian.eduucenter.annotations.LoginCount)")
    public void loginCountPointCut(){}
    /**
     * 定义登录切点
     */
    @Pointcut("@annotation(com.xian.eduucenter.annotations.RegisterCount)")
    public void registerCountPointCut(){}

    /**
     * 切入处理，环绕通知
     * @param point
     * @return
     */
    @Around("loginCountPointCut()")
    public Object afterLoginMethod(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Object obj = point.proceed();
        try {
            R result = (R) obj;
            if (result.getSuccess()) {
                LoginVo loginVo = (LoginVo) args[0];
                redisUtil.pfAdd(ConstantUtil.LOGIN_COUNT_KEY, loginVo.getMobile());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return obj;
        }
        return obj;
    }

    /**
     * 切入处理，环绕通知
     * @param joinPoint
     * @return
     */
    @Around("registerCountPointCut()")
    public Object afterRegisteMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object obj = joinPoint.proceed();
        try {
            R result = (R) obj;
            if (result.getSuccess()) {
                RegisterVo member = (RegisterVo) args[0];
                redisUtil.pfAdd(ConstantUtil.REGISTER_COUNT_KEY, member.getMobile());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return obj;
        }
        return obj;
    }
}
