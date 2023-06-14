package com.xian.aclservice.service;

import com.xian.aclservice.entities.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.aclservice.entities.vo.RolePermissionVo;
import com.xian.entities.R;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
public interface RoleService extends IService<Role> {

    List<Role> getRoleByUserId(String id);

    List<String> getRoleNameByUserId(String id);

    List<String> getRoleIdsByUserId(String id);

    R getPermissionIds(String roleId);

    R updateRoleAndPermission(RolePermissionVo rolePermission);

    R delRole(String roleId);

    R delRoleBatch(List<String> roleIds);

    R addRolePermission(RolePermissionVo rolePermissionVo);

    void updateRoleByUserId(String id, List<String> roleIds);

    void saveRoleByUserId(String id, List<String> roleIds);

    void delRoleByUserid(String userId);

    void delRoleByUserIds(List<String> userIds);
}
