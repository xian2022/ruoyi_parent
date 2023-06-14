package com.xian.aclservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.aclservice.entities.RolePermission;
import com.xian.aclservice.mapper.RolePermissionMapper;
import com.xian.aclservice.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色权限 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {
    @Override
    public boolean updateRolePermission(String id, List<String> permissionIds) {
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id",id);
        List<RolePermission> saveList = new ArrayList<>();
        try {
            remove(rolePermissionQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (permissionIds.size()>0) {
            permissionIds.forEach(permissionId -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(id);
                rolePermission.setPermissionId(permissionId);
                saveList.add(rolePermission);
            });
            return saveBatch(saveList);
        }
        return true;
    }

    @Override
    public void saveRolePermission(String id, List<String> permissionIds) {
        if (permissionIds.size() > 0) {
            List<RolePermission> rolePermissions = new ArrayList<>();
            permissionIds.forEach(permissionId -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(id);
                rolePermission.setPermissionId(permissionId);
                rolePermissions.add(rolePermission);
            });
            saveBatch(rolePermissions);
        }
    }
}
