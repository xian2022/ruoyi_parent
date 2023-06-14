package com.xian.aclservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.aclservice.entities.Role;
import com.xian.aclservice.entities.vo.RoleVo;
import com.xian.aclservice.entities.vo.UserInfo;
import com.xian.aclservice.service.PermissionService;
import com.xian.aclservice.service.RoleService;
import com.xian.aclservice.service.UserService;
import com.xian.security.entites.SecurityUser;
import com.xian.security.entites.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        com.xian.aclservice.entities.User user = userService.getOne(new QueryWrapper<com.xian.aclservice.entities.User>().eq("username", username));
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }
        User loginUser = BeanUtil.copyProperties(user, User.class);
        List<String> permissionsValues = getUserPermissions(loginUser.getId());
        return new SecurityUser(loginUser,permissionsValues);
    }

    private List<String> getUserPermissions(String userId) {
        List<Role> roles = roleService.getRoleByUserId(userId);
        List<RoleVo> roleVos = null;
        List<String> permissionsValues = new ArrayList<>();
        if (ObjectUtil.isNotNull(roles) && roles.size()>0) {
            roleVos = BeanUtil.copyToList(roles, RoleVo.class);
            List<String> roleIds = roleVos.stream().map(RoleVo::getId).collect(Collectors.toList());
            if (ObjectUtil.isNotNull(roleIds) && roleIds.size()>0) {
                permissionsValues = permissionService.getPermissionValuesByRoleIds(roleIds);
            }
        }
        return permissionsValues;
    }
}
