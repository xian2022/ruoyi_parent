package com.xian.aclservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.xian.aclservice.entities.Role;
import com.xian.aclservice.entities.User;
import com.xian.aclservice.entities.vo.PermissionVo;
import com.xian.aclservice.entities.vo.RoleVo;
import com.xian.aclservice.entities.vo.UserInfo;
import com.xian.aclservice.entities.vo.UserLogin;
import com.xian.aclservice.helper.MenuHelper;
import com.xian.aclservice.service.IndexService;
import com.xian.aclservice.service.RoleService;
import com.xian.aclservice.service.UserService;
import com.xian.entities.R;
import com.xian.security.entites.SecurityUser;
import com.xian.utils.ConstantUtil;
import com.xian.utils.JwtUtil;
import com.xian.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PermissionServiceImpl permissionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Override
    public R getUserInfo(HttpServletRequest request) {
        String userId = null;
        try {
            userId = JwtUtil.getIdByJwtToken(request);
        } catch (Exception e) {
            return R.fail().code(50014);
        }
        String admin = redisUtil.get(ConstantUtil.LOGIN_ADMIN_KEY + userId);
        if (!StringUtils.hasText(admin)) {
            return R.fail().code(50014);
        }
        User user = userService.getById(userId);
        if (ObjectUtil.isNull(user)) {
            return R.fail().code(50008);
        }
        return R.ok().data(getUserPermissionAndRoles(user));
    }

    private Map<String, Object> getUserPermissionAndRoles(User user) {
        List<Role> roles = roleService.getRoleByUserId(user.getId());
        List<RoleVo> roleVos = null;
        List<String> permissionsValues = null;
        if (ObjectUtil.isNotNull(roles) && roles.size()>0) {
            roleVos = BeanUtil.copyToList(roles, RoleVo.class);
            List<String> roleIds = roleVos.stream().map(RoleVo::getId).collect(Collectors.toList());
            if (ObjectUtil.isNotNull(roleIds) && roleIds.size()>0) {
                permissionsValues = permissionService.getPermissionValuesByRoleIds(roleIds);
            }
        }
        UserInfo userInfo = BeanUtil.copyProperties(user, UserInfo.class);
        Map<String, Object> map = new HashMap<>();
        userInfo.setRoles(roleVos);
        map.put("userInfo",userInfo);
        map.put("permissionsValues",permissionsValues);
        return map;
    }

    @Override
    public List<JSONObject> getMenu(HttpServletRequest request) {
        String userId = JwtUtil.getIdByJwtToken(request);
        List<String> roleIds = roleService.getRoleIdsByUserId(userId);
        List<PermissionVo> permissionVos = BeanUtil.copyToList(permissionService.getPermissionByRoleIds(roleIds), PermissionVo.class);
        List<PermissionVo> menuByStream = permissionService.getMenuByStream(permissionVos, "0");
        List<JSONObject> bulid = MenuHelper.bulid(menuByStream);
        return bulid;
    }

    @Override
    public R login(UserLogin userLogin) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userLogin.getUsername(),userLogin.getPassword(),new ArrayList<>());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (Objects.isNull(authenticate)) {
            return R.fail();
        }
        SecurityUser loginUser = BeanUtil.copyProperties(authenticate.getPrincipal(), SecurityUser.class);
        User user = BeanUtil.copyProperties(loginUser.getLoginUserInfo(), User.class);
        String userId = user.getId();
        String token = JwtUtil.getUserJwtToken(userId, user.getUsername());
        redisUtil.set(ConstantUtil.LOGIN_ADMIN_KEY+userId, JSONUtil.toJsonStr(loginUser),30L, TimeUnit.MINUTES);
        return R.ok().data("Authentication",token);
    }

    @Override
    public R logout() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        SecurityUser loginUser = (SecurityUser) authentication.getPrincipal();
        String userId = loginUser.getLoginUserInfo().getId();
        redisUtil.delete(ConstantUtil.LOGIN_ADMIN_KEY+userId);
        log.info(userId+"登出成功！");
        return R.ok().message("登出成功！");
    }
}
