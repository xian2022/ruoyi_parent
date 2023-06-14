package com.xian.aclservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.aclservice.entities.Permission;
import com.xian.aclservice.entities.Role;
import com.xian.aclservice.entities.UserRole;
import com.xian.aclservice.entities.vo.RolePermissionVo;
import com.xian.aclservice.mapper.RoleMapper;
import com.xian.aclservice.service.PermissionService;
import com.xian.aclservice.service.RolePermissionService;
import com.xian.aclservice.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.aclservice.service.UserRoleService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Override
    public List<Role> getRoleByUserId(String userId) {
        List<String> roleIds = roleMapper.getRoleIdByUserId(userId);
        if (roleIds.size() < 1)
            return null;
        return listByIds(roleIds);
    }

    @Override
    public List<String> getRoleNameByUserId(String id) {
        List<Role> roles = getRoleByUserId(id);
        if (ObjectUtil.isNull(roles)) {
            return null;
        }
        return roles.stream().map(Role::getRoleName).collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleIdsByUserId(String id) {
        List<Role> roles = getRoleByUserId(id);
        if (ObjectUtil.isNull(roles)) {
            return null;
        }
        return roles.stream().map(Role::getId).collect(Collectors.toList());
    }

    @Override
    public R getPermissionIds(String roleId) {
        List<String> permissionIds = roleMapper.getPermissionIds(roleId);
        List<String> permissionChildren = null;
        if (permissionIds.size() > 0) {
            permissionChildren = permissionService.getPermissionChildren(permissionIds);
        }
        return R.ok().data("list",permissionChildren);
    }

    @Transactional
    @Override
    public R updateRoleAndPermission(RolePermissionVo rolePermission) {
        if (ObjectUtil.isNull(rolePermission)) {
            return R.fail().message("传递非法参数！");
        }
        Role role = BeanUtil.copyProperties(rolePermission, Role.class);
        String id = role.getId();
        List<String> permissionIds = rolePermission.getPermissionIds();
        if (ObjectUtil.isNotNull(permissionIds) && permissionIds.size()>0) {
            rolePermissionService.updateRolePermission(id,permissionIds);
        }
        updateById(role);
        return R.ok();
    }

    @Transactional
    @Override
    public R delRole(String roleId) {
        List<String> strings = new ArrayList<>();
        strings.add(roleId);
        return delRoleBatch(strings);
    }

    @Transactional
    @Override
    public R delRoleBatch(List<String> roleIds) {
        if (roleIds.size() < 1)
            return R.fail();
        try {
            permissionService.delPermissionByRoleIds(roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail();
        }
        return R.returnR(removeBatchByIds(roleIds));
    }

    @Transactional
    @Override
    public R addRolePermission(RolePermissionVo rolePermissionVo) {
        Role role = BeanUtil.copyProperties(rolePermissionVo, Role.class);
        try {
            save(role);
            String id = role.getId();
            rolePermissionService.saveRolePermission(id,rolePermissionVo.getPermissionIds());
        } catch (Exception e) {
            return R.fail();
        }
        return R.ok();
    }

    @Override
    public void updateRoleByUserId(String id, List<String> roleIds) {
        delRoleByUserId(id);
        saveRoleUser(id, roleIds);
    }

    @Override
    public void saveRoleByUserId(String id, List<String> roleIds) {
        saveRoleUser(id, roleIds);
    }

    @Override
    public void delRoleByUserid(String userId) {
        delRoleByUserId(userId);
    }

    @Override
    public void delRoleByUserIds(List<String> userIds) {
        userRoleService.remove(new QueryWrapper<UserRole>().in("user_id", userIds));
    }

    private void delRoleByUserId(String id) {
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", id));
    }

    private void saveRoleUser(String id, List<String> roleIds) {
        if (roleIds.size()>0) {
            List<UserRole> list = new ArrayList<>();
            roleIds.forEach(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                list.add(userRole);
            });
            userRoleService.saveBatch(list);
        }
    }
}
