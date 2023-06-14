package com.xian.aclservice.controller;

import com.xian.aclservice.entities.vo.UserLogin;
import com.xian.aclservice.service.IndexService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/acl/index")
public class IndexController {
    @Autowired
    private IndexService indexService;

    /**
     * 根据token获取用户信息
     */

    @PostMapping("login")
    public R login(@RequestBody UserLogin userLogin){
        return indexService.login(userLogin);
    }

    @GetMapping("info")
    public R info(HttpServletRequest request){
        //获取当前登录用户信息
        return indexService.getUserInfo(request);
    }

    /**
     * 获取菜单
     * @return
     */
    @GetMapping("menu")
    public R getMenu(HttpServletRequest request){
        //获取当前登录用户菜单
        return R.ok().data("permissionList", indexService.getMenu(request));
    }

    @PostMapping("logout")
    public R logout(){
        return indexService.logout();
    }
}
