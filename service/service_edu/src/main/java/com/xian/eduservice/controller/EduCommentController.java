package com.xian.eduservice.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduservice.entities.Comment;
import com.xian.eduservice.entities.vo.CommentVo;
import com.xian.eduservice.service.CommentService;
import com.xian.utils.ConstantUtil;
import com.xian.utils.JwtUtil;
import com.xian.entities.R;
import com.xian.utils.RedisUtil;
import com.xian.servicebase.handler.RuoyiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/06/20 08:36
 */
@CrossOrigin
@RestController
@RequestMapping("/edu/comment")
public class EduCommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisUtil redisUtil;
    @PostMapping("/commit")
    public R commitComment (@RequestBody CommentVo comment, HttpServletRequest request) {
        String memberId = JwtUtil.getIdByJwtToken(request);
        String s = redisUtil.get(ConstantUtil.LOGIN_USER_KEY + memberId);
        if (!StringUtils.hasText(s)) {
            throw new RuoyiException(20001,"登录过期！请重新登陆");
        }
        return R.returnR(commentService.save(BeanUtil.copyProperties(comment,Comment.class)));
    }
    @GetMapping("/course/{courseId}/{current}")
    public R getCourseComment (@PathVariable("courseId") String courseId,@PathVariable("current") Integer current) {
        return pageList(current,5,courseId);
    }

    @GetMapping("/del/{commentId}/{memberId}")
    public R delComment (@PathVariable("commentId") String commentId,@PathVariable("memberId") String memberId) {
        QueryWrapper<Comment> query = new QueryWrapper<>();
        query.eq("id",commentId).eq("member_id",memberId);
        return R.returnR(commentService.remove(query));
    }

    private R pageList(Integer current, Integer count, String courseId) {
        return commentService.getCourseComment(current,count,courseId);
    }
}
