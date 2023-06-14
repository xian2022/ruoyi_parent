package com.xian.aclservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.aclservice.entities.Role;
import com.xian.aclservice.entities.vo.RolePermissionVo;
import com.xian.aclservice.entities.vo.RoleQuery;
import com.xian.aclservice.service.RoleService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@RestController
@RequestMapping("/admin/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @PostMapping("/all/{current}/{count}")
    public R getAllRole(@PathVariable("current")Integer current, @PathVariable("count") Integer count, @RequestBody RoleQuery roleQuery) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (roleQuery!=null) {
            String name = roleQuery.getRoleName();
            if (!ObjectUtils.isEmpty(name)) {
                wrapper.like("role_name",name);
            }
            wrapper.orderByDesc("gmt_create");
        }
        return pageList(current,count,wrapper);
    }
    @GetMapping("/all")
    public R getAll() {
        List<Role> list = roleService.list();
        return R.ok().data("items",list);
    }

    @GetMapping("/getPermissionIds/{roleId}")
    public R getPermissionIds(@PathVariable("roleId")String roleId) {
        return roleService.getPermissionIds(roleId);
    }

    @PostMapping
    public R updateRoleAndPermission(@RequestBody RolePermissionVo rolePermissionVo) {
        return roleService.updateRoleAndPermission(rolePermissionVo);
    }

    @DeleteMapping("/{roleId}")
    public R delRole(@PathVariable("roleId") String roleId) {
        return roleService.delRole(roleId);
    }

    @DeleteMapping
    public R delRoleBatch(@RequestBody List<String> roleIds) {
        return roleService.delRoleBatch(roleIds);
    }

    @PostMapping("/add")
    public R addRolePermission(@RequestBody RolePermissionVo rolePermissionVo) {
        return roleService.addRolePermission(rolePermissionVo);
    }

    private R pageList(Integer current, Integer count, QueryWrapper<Role> wrapper) {
        Page<Role> page = new Page<>(current,count);
        roleService.page(page,wrapper);
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("items",page.getRecords());
    }
}
