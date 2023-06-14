package com.xian.educms.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.educms.entities.Banner;
import com.xian.educms.entities.vo.BannerVo;
import com.xian.educms.service.BannerService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前台控制接口
 * </p>
 *
 * @author xian
 * @since 2022/05/27 16:31
 */
@CrossOrigin
@RestController
@RequestMapping("/educms/front/banner")
public class BannerFrontController {
    @Autowired
    private BannerService bannerService;

    @GetMapping("getAll")
    public R getAllBanner() {
        return R.ok().data("list",bannerService.selectAllBanner());
    }

}
