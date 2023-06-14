package com.xian.educms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.educms.entities.Banner;
import com.xian.educms.entities.vo.BannerVo;
import com.xian.educms.service.BannerService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 后台控制接口
 * </p>
 *
 * @author xian
 * @since 2022/05/27 16:31
 */
@CrossOrigin
@RestController
@RequestMapping("/educms/admin/banner")
public class BannerAdminController {
    @Autowired
    private BannerService bannerService;
    // 分页查询banner
    @PostMapping("/pageBanner/{page}/{size}")
    public R pageBanner(@PathVariable(value = "page",required = true) Integer page, @PathVariable(value = "size",required = true) Integer size, @RequestBody(required = false) BannerVo bannerVo) {
        QueryWrapper<Banner> wrapper = new QueryWrapper<>();
        if (bannerVo != null) {
            String title = bannerVo.getTitle();
            if (!ObjectUtils.isEmpty(title)) {
                wrapper.like("title",title);
            }
            wrapper.orderByAsc("sort");
        }
        return pageList(page,size,wrapper);
    }

    // 删除Banner
    @CacheEvict(value = "banner", allEntries = true)
    @DeleteMapping("/del/{id}")
    public R delBannerLogic(@PathVariable("id") String id) {
        return R.returnR(bannerService.removeById(id));
    }

    // 修改Banner
    @CacheEvict(value = "banner", allEntries = true)
    @PutMapping("/mod")
    public R updateById(@RequestBody Banner banner) {
        return R.returnR(bannerService.updateById(banner));
    }

    // 添加Banner
    @CacheEvict(value = "banner", allEntries = true)
    @PostMapping("/add")
    public R addBanner(@RequestBody Banner banner) {
        return R.returnR(bannerService.save(banner));
    }

    // 根据Id获取Banner
    @GetMapping("/getBannerById/{id}")
    public R getBannerById(@PathVariable("id") String id) {
        return R.ok().data("item",bannerService.getById(id));
    }

    private R pageList(Integer current, Integer count, QueryWrapper<Banner> wrapper) {
        Page<Banner> page = new Page<>(current,count);
        bannerService.page(page,wrapper);
        return R.ok().data("total",page.getTotal())
                .data("pages",page.getPages())
                .data("current",page.getCurrent())
                .data("items",page.getRecords());
    }
}
