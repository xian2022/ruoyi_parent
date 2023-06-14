package com.xian.aclservice.service;

import com.xian.aclservice.entities.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色权限 服务类
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
public interface RolePermissionService extends IService<RolePermission> {

    boolean updateRolePermission(String id, List<String> permissionIds);

    void saveRolePermission(String id, List<String> permissionIds);
}
