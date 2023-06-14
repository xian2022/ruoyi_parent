package com.xian.eduservice.service;

import com.xian.eduservice.entities.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.entities.R;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author xian
 * @since 2022/06/20 08:36
 */
public interface CommentService extends IService<Comment> {

    R getCourseComment(Integer current, Integer count, String courseId);
}
