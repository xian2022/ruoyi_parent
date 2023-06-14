package com.xian.statistics.schedule;

import com.xian.entities.R;
import com.xian.statistics.service.DailyService;
import com.xian.utils.ConstantUtil;
import com.xian.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@Slf4j
public class DailyTasks {
    @Autowired
    private DailyService dailyService;
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 定时更新问题浏览量到数据库中
     * 每天凌晨两点跑一次
     */
    @Scheduled(cron = "50 59 23 * * *")
    @Transactional(rollbackFor = Exception.class)
    public void updateQuestionView() {
        log.info("start to update daily Count...");
        R result = dailyService.createTodayStatistics();
        if (result.getSuccess()) {
            String[] keys = {ConstantUtil.LOGIN_COUNT_KEY,ConstantUtil.REGISTER_COUNT_KEY,ConstantUtil.VIDEO_VIEW_KEY,ConstantUtil.COURSE_PUBLISH_KEY};
            redisUtil.delete(Arrays.asList(keys));
            log.info("update daily Count finish! ");
        } else {
            log.info("update daily Count error! ");
        }
    }
}
