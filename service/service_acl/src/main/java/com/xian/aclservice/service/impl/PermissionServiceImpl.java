package com.xian.aclservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.aclservice.entities.Permission;
import com.xian.aclservice.entities.Role;
import com.xian.aclservice.entities.vo.PermissionVo;
import com.xian.aclservice.mapper.PermissionMapper;
import com.xian.aclservice.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public List<Permission> getPermissionByRoleIds(List<String> roleIds) {
        if (roleIds.size()<1)
            return null;
        List<String> ids = permissionMapper.getPermissionIdsByRoleIdList(roleIds);
        if (ids.size()<1)
            return null;
        return listByIds(ids);
    }

    @Override
    public List<String> getPermissionValuesByRoleIds(List<String> roleIds) {
        List<Permission> permissions = getPermissionByRoleIds(roleIds);
        if (ObjectUtil.isNull(permissions))
            return null;
        return permissions.stream().map(Permission::getPermissionValue).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public R getPermissionMenu() {
        return R.ok().data("menu",getMenu("0"));
    }

    @Override
    public R getPermissionMenuByIdBatch(List<String> permissionIds) {
        List<Permission> permissions;
        List<PermissionVo> menuByStream = null;
        if (permissionIds.size() > 0) {
            permissions = listByIds(permissionIds);
            List<PermissionVo> permissionVos = BeanUtil.copyToList(permissions, PermissionVo.class);
            menuByStream = getMenuByStream(permissionVos, "0");
        }
        return R.ok().data("menu",menuByStream);
    }

    @Override
    public List<String> getPermissionChildren(List<String> permissionIds) {
        List<Permission> permissions = listByIds(permissionIds);
        List<String> children = new ArrayList<>();
        getPermissionChildren(permissions,"0",children);
        return children;
    }

    @Override
    public void delPermissionByRoleIds(List<String> roleIds) {
        if (roleIds.size() > 0) {
            permissionMapper.deletePermissionByRoleIds(roleIds);
        }
    }

    @Override
    public R addPermissionMenu(Permission permission) {
        save(permission);
        return R.ok();
    }

    @Override
    public R delMenuByIds(List<String> ids) {
        List<Permission> permissions = listByIds(ids);
        return R.returnR(delBatch(permissions));
    }

    @Override
    public R modifyPermissionMenu(Permission permission) {
        updateById(permission);
        return R.ok();
    }

    private boolean delBatch(List<Permission> permissions) {
        for (Permission permission : permissions) {
            String id = permission.getId();
            List<Permission> children = list(new QueryWrapper<Permission>().eq("pid",id));
            if (children!=null && children.size() > 0) {
                delBatch(children);
            }
            removeById(id);
        }
        return true;
    }

    private void savePermission(PermissionVo permissionVo, String pid) {
        Permission permission = BeanUtil.copyProperties(permissionVo, Permission.class);
        permission.setPid(pid);
        save(permission);
        String id = permission.getId();
        List<PermissionVo> children = permissionVo.getChildren();
        if (children.size()>0) {
            children.forEach(permissionVo1 -> {
                savePermission(permissionVo1,id);
            });
        }
    }

    private void getPermissionChildren(List<Permission> permissions, String pid, List<String> children) {
        List<Permission> collect = permissions.stream().filter(permission -> pid.equals(permission.getPid())).collect(Collectors.toList());
        if (collect.size() > 0) {
            collect.forEach(permission -> getPermissionChildren(permissions,permission.getId(),children));
        } else {
            children.add(pid);
        }
    }

    private List<PermissionVo> getMenu(String pid) {
        QueryWrapper<Permission> permissionWrapper = new QueryWrapper<>();
        permissionWrapper.eq("pid",pid);
        List<Permission> list = list(permissionWrapper);
        List<PermissionVo> permissionVos = BeanUtil.copyToList(list, PermissionVo.class);
        if (permissionVos.size()>0) {
            permissionVos.forEach(permissionVo -> {
                String id = permissionVo.getId();
                permissionVo.setChildren(getMenu(id));
            });
        }
        return permissionVos;
    }

    public List<PermissionVo> getMenuByStream(List<PermissionVo> permissions,String pid) {
        List<PermissionVo> collect = permissions.stream().filter(permissionVo -> pid.equals(permissionVo.getPid())).collect(Collectors.toList());
        if (collect.size()>0) {
            collect.forEach(permissionVo -> {
                String id = permissionVo.getId();
                permissionVo.setChildren(getMenuByStream(permissions,id));
            });
        }
        return collect;
    }
}
