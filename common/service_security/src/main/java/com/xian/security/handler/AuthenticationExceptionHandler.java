package com.xian.security.handler;

import cn.hutool.json.JSONUtil;
import com.xian.entities.R;
import com.xian.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String jsonStr = JSONUtil.toJsonStr(R.fail().message("用户认证失败！请重新登录！").code(50014));
        // 处理异常
        ResponseUtil.out(response,jsonStr);
    }
}
