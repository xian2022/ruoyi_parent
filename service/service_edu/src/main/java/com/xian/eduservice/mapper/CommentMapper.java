package com.xian.eduservice.mapper;

import com.xian.eduservice.entities.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author xian
 * @since 2022/06/20 08:36
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
