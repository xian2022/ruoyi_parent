package com.xian.aclservice.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.aclservice.entities.Permission;
import com.xian.aclservice.entities.Role;
import com.xian.aclservice.entities.vo.PermissionQuery;
import com.xian.aclservice.entities.vo.PermissionVo;
import com.xian.aclservice.entities.vo.RoleQuery;
import com.xian.aclservice.service.PermissionService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@RestController
@RequestMapping("/admin/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @GetMapping("/getPermissionMenu")
    public R getPermissionMenu () {
//        if (StringUtils.hasText(name)) {
//            QueryWrapper<Permission> wrapper = new QueryWrapper<>();
//            wrapper.like("name",name);
//            return R.ok().data("menu",permissionService.list(wrapper));
//        }
//        if (ObjectUtil.isNotNull(permissionQuery)) {
//            String name = permissionQuery.getName();
//            QueryWrapper<Permission> wrapper = new QueryWrapper<>();
//            if (StringUtils.hasText(name)) {
//                wrapper.like("name",name);
//                return R.ok().data("menu",permissionService.list(wrapper));
//            }
//        }
        return permissionService.getPermissionMenu();
    }
    @PostMapping("/getPermissionMenuByIdBatch")
    public R getPermissionMenuByIdBatch (@RequestBody List<String> permissionIds) {
        return permissionService.getPermissionMenuByIdBatch(permissionIds);
    }

    @PostMapping("/add")
    public R addPermissionMenu (@RequestBody Permission permission) {
        return permissionService.addPermissionMenu(permission);
    }

    @PostMapping("/modify")
    public R modifyPermissionMenu (@RequestBody Permission permission) {
        return permissionService.modifyPermissionMenu(permission);
    }

    @PostMapping("/delMenuByIds")
    public R delMenuByIds (@RequestBody List<String> ids) {
        return permissionService.delMenuByIds(ids);
    }
}
