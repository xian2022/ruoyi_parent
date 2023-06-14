package com.xian.aclservice.service.impl;

import com.xian.aclservice.entities.User;
import com.xian.aclservice.mapper.UserMapper;
import com.xian.aclservice.service.RoleService;
import com.xian.aclservice.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RoleService roleService;
    @Override
    public void delUserAndRole(String userId) {
        removeById(userId);
        roleService.delRoleByUserid(userId);
    }

    @Override
    public void delUserAndRoles(List<String> userIds) {
        removeBatchByIds(userIds);
        roleService.delRoleByUserIds(userIds);
    }
}
