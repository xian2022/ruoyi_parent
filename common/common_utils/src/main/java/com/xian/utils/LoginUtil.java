package com.xian.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginUtil {
    @Autowired
    private RedisUtil redisUtil;
    public boolean isLogin(HttpServletRequest request) {
        String memberId = JwtUtil.getIdByJwtToken(request);
        String json = redisUtil.get(ConstantUtil.LOGIN_USER_KEY + memberId);
        return StringUtils.hasText(json);
    }
}
