package com.xian.eduservice.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xian.eduservice.entities.Course;
import com.xian.eduservice.entities.Video;
import com.xian.eduservice.service.CourseService;
import com.xian.eduservice.service.VideoService;
import com.xian.utils.ConstantUtil;
import com.xian.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
public class VideoTasks {
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private VideoService videoService;
    /**
     * 定时更新问题浏览量到数据库中
     * 每天凌晨两点跑一次
     */
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional(rollbackFor = Exception.class)
    public void updateQuestionView() {
        log.info("start to update video play Count...");
        Set<ZSetOperations.TypedTuple<String>> videoSet = redisUtil.zRangeWithScores(ConstantUtil.VIDEO_PLAY_KEY, 0, -1);
        videoSet.forEach(videoPlay -> {
            LambdaUpdateWrapper<Video> videoUpdateWrapper = new LambdaUpdateWrapper<>();
            videoUpdateWrapper.eq(Video::getId,videoPlay.getValue()).setSql("play_count = play_count + " + videoPlay.getScore());
            videoService.update(videoUpdateWrapper);
        });
        redisUtil.delete(ConstantUtil.VIDEO_PLAY_KEY);
        log.info("update view Count finish! ");
    }
}
