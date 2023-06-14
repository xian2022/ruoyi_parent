package com.xian.aclservice.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.aclservice.entities.Role;
import com.xian.aclservice.entities.User;
import com.xian.aclservice.entities.vo.RoleQuery;
import com.xian.aclservice.entities.vo.RoleVo;
import com.xian.aclservice.entities.vo.UserInfo;
import com.xian.aclservice.entities.vo.UserQuery;
import com.xian.aclservice.service.RoleService;
import com.xian.aclservice.service.UserService;
import com.xian.entities.R;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @PostMapping("/all/{current}/{count}")
    public R getAllUser(@PathVariable("current")Integer current, @PathVariable("count") Integer count, @RequestBody UserQuery userQuery) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (userQuery!=null) {
            String username = userQuery.getUsername();
            String nickName = userQuery.getNickName();
            if (!ObjectUtils.isEmpty(username)) {
                wrapper.like("username",username);
            } else if (!ObjectUtils.isEmpty(nickName)) {
                wrapper.like("nick_name",nickName);
            }
            wrapper.orderByDesc("gmt_create");
        }
        return pageList(current,count,wrapper);
    }

    @PostMapping("/delBatch")
    public R getAllUser(@RequestBody List<String> userIds) {
        userService.delUserAndRoles(userIds);
        return R.ok();
    }

    @PostMapping("/update")
    public R updateUser(@RequestBody UserInfo userInfo) {
        if (!ObjectUtils.isEmpty(userInfo)) {
            User user = userService.getById(userInfo.getId());
            String nickName = userInfo.getNickName();
            String newPassword = userInfo.getNewPassWord();
            String avatar = userInfo.getAvatar();
            List<String> roleIds = userInfo.getRoleIds();
            if (!StrUtil.isEmpty(newPassword)) {
                user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            } else if (!StrUtil.isEmpty(nickName) && !nickName.equals(user.getNickName())) {
                user.setNickName(nickName);
            } else if (!StrUtil.isEmpty(avatar) && !avatar.equals(user.getAvatar())) {
                user.setAvatar(avatar);
            }
            try {
                userService.updateById(user);
                roleService.updateRoleByUserId(user.getId(),roleIds);
            } catch (Exception e) {
                e.printStackTrace();
                return R.fail();
            }
        }
        return R.ok();
    }

    @PostMapping("/add")
    public R addUser(@RequestBody UserInfo userInfo) {
        User user = BeanUtil.copyProperties(userInfo, User.class);
        user.setPassword(userInfo.getNewPassWord());
        List<String> roleIds = userInfo.getRoleIds();
        try {
            userService.save(user);
            roleService.saveRoleByUserId(user.getId(),roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail();
        }
        return R.ok();
    }

    @DeleteMapping("/{userId}")
    public R delUser(@PathVariable("userId") String userId) {
        userService.delUserAndRole(userId);
        return R.ok();
    }

    private R pageList(Integer current, Integer count, QueryWrapper<User> wrapper) {
        Page<User> page = new Page<>(current,count);
        userService.page(page,wrapper);
        List<User> records = page.getRecords();
        List<UserInfo> userInfos = BeanUtil.copyToList(records, UserInfo.class);
        if (userInfos.size()>0) {
            userInfos.forEach(userInfo -> {
                String id = userInfo.getId();
                userInfo.setRoles(BeanUtil.copyToList(roleService.getRoleByUserId(id), RoleVo.class));
            });
        }
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("items",userInfos);
    }

}
