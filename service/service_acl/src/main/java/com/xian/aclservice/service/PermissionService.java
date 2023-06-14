package com.xian.aclservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.aclservice.entities.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.aclservice.entities.Role;
import com.xian.aclservice.entities.vo.PermissionVo;
import com.xian.entities.R;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
public interface PermissionService extends IService<Permission> {

    List<Permission> getPermissionByRoleIds(List<String> roleIds);

    List<String> getPermissionValuesByRoleIds(List<String> roleIds);

    R getPermissionMenu();

    R getPermissionMenuByIdBatch(List<String> permissionIds);

    List<String> getPermissionChildren(List<String> permissionIds);

    void delPermissionByRoleIds(List<String> roleIds);

    R addPermissionMenu(Permission permission);

    R delMenuByIds(List<String> ids);

    R modifyPermissionMenu(Permission permission);
}
