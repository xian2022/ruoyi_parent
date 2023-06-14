package com.xian.eduservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.eduservice.entities.Comment;
import com.xian.eduservice.entities.vo.CommentVo;
import com.xian.eduservice.mapper.CommentMapper;
import com.xian.eduservice.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.entities.R;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/06/20 08:36
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public R getCourseComment(Integer current, Integer count, String courseId) {
        Page<Comment> page = new Page<>(current,count);
        page(page,new QueryWrapper<Comment>().eq("course_id",courseId).eq("is_deleted",0).orderByDesc("gmt_create"));
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("items", BeanUtil.copyToList(page.getRecords(), CommentVo.class));
    }
}
