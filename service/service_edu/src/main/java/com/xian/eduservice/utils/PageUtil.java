package com.xian.eduservice.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.eduservice.entities.Teacher;
import com.xian.entities.R;
import org.springframework.stereotype.Component;

@Component
public class PageUtil<T> {
    public R pageList(Integer current, Integer count, QueryWrapper<Teacher> wrapper, IService service) {
        Page<T> page = new Page<>(current,count);
        service.page(page,wrapper);
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("items",page.getRecords());
    }
}
