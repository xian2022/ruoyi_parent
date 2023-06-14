package com.xian.security.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.ObjectMapper;
import com.xian.security.entites.SecurityUser;
import com.xian.security.entites.User;
import com.xian.utils.ConstantUtil;
import com.xian.utils.JwtUtil;
import com.xian.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/admin/acl/index/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        //获取token
        String token = request.getHeader("Authentication");
        if (StringUtils.hasText(token)) {
            String userId;
            //解析token
            try {
                userId = JwtUtil.getIdByJwtToken(request);
            } catch (Exception e) {
                filterChain.doFilter(request,response);
                return;
            }
            if (StringUtils.hasText(userId)) {
                String userJson = redisUtil.get(ConstantUtil.LOGIN_ADMIN_KEY + userId);
                if (StringUtils.hasText(userJson)) {
                    SecurityUser loginUser = JSONUtil.toBean(userJson,SecurityUser.class);
                    if (ObjectUtil.isNotNull(loginUser.getLoginUserInfo())) {
                        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginUser,token,loginUser.getAuthorities()));
                    }
                }
            }
        }
        //从redis中获取用户信息
        //存入SecurityContextHolder
        filterChain.doFilter(request, response);
    }
}
