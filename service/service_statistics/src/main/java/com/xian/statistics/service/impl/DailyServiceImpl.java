package com.xian.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.entities.R;
import com.xian.utils.RedisUtil;
import com.xian.statistics.entities.Daily;
import com.xian.statistics.mapper.DailyMapper;
import com.xian.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.utils.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/06/28 23:35
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public R countRegister(String day) {
        if (isToDay(day)) {
            return R.ok().data("registerCount",redisUtil.pfCount(ConstantUtil.REGISTER_COUNT_KEY));
        }
        QueryWrapper<Daily> query = new QueryWrapper<>();
        query.eq("date_calculated",day);
        return R.ok().data("registerCount",getCountByCondition(query).getRegisterNum());
    }

    @Override
    public R countLogin(String day) {
        if (isToDay(day)) {
            return R.ok().data("loginCount",redisUtil.pfCount(ConstantUtil.LOGIN_COUNT_KEY));
        }
        QueryWrapper<Daily> query = new QueryWrapper<>();
        query.eq("date_calculated",day);
        return R.ok().data("loginCount",getCountByCondition(query).getLoginNum());
    }

    @Override
    public R countCourse(String day) {
        if (isToDay(day)) {
            return R.ok().data("courseCount",redisUtil.pfCount(ConstantUtil.COURSE_PUBLISH_KEY));
        }
        QueryWrapper<Daily> query = new QueryWrapper<>();
        query.eq("date_calculated",day);
        return R.ok().data("courseCount",getCountByCondition(query).getCourseNum());
    }

    @Override
    public R countVideo(String day) {
        if (isToDay(day)) {
            return R.ok().data("videoCount",redisUtil.pfCount(ConstantUtil.VIDEO_VIEW_KEY));
        }
        QueryWrapper<Daily> query = new QueryWrapper<>();
        query.eq("date_calculated",day);
        return R.ok().data("videoCount",getCountByCondition(query).getVideoViewNum());
    }

    @Override
    public R getStatistics(String day) {
        if (isToDay(day)) {
            Daily daily = getTodayStatistics();
            return R.ok().data("sta",daily);
        }
        QueryWrapper<Daily> query = new QueryWrapper<>();
        query.eq("date_calculated",day);
        return R.ok().data("sta",getCountByCondition(query));
    }

    @Override
    public R createTodayStatistics() {
        Daily sta = getTodayStatistics();
        return save(sta)?R.ok().data("sta",sta):R.fail();
    }

    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {
        QueryWrapper<Daily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.select(type, "date_calculated");
        dayQueryWrapper.between("date_calculated", begin, end);
        List<Daily> dayList = baseMapper.selectList(dayQueryWrapper);

        Map<String, Object> map = new HashMap<>();
        List<Integer> dataList = new ArrayList<Integer>();
        List<String> dateList = new ArrayList<String>();
        map.put("dataList", dataList);
        map.put("dateList", dateList);

        for (int i = 0; i < dayList.size(); i++) {
            Daily daily = dayList.get(i);
            dateList.add(daily.getDateCalculated());
            switch (type) {
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        return map;
    }

    private Daily getTodayStatistics() {
        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Daily daily = new Daily();
        daily.setDateCalculated(day);
        daily.setRegisterNum(redisUtil.pfCount(ConstantUtil.REGISTER_COUNT_KEY).intValue());
        daily.setLoginNum(redisUtil.pfCount(ConstantUtil.LOGIN_COUNT_KEY).intValue());
        daily.setCourseNum(redisUtil.pfCount(ConstantUtil.COURSE_PUBLISH_KEY).intValue());
        daily.setVideoViewNum(redisUtil.pfCount(ConstantUtil.VIDEO_VIEW_KEY).intValue());
        return daily;
    }

    private boolean isToDay(String day) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return today.equals(day);
    }
    private Daily getCountByCondition(QueryWrapper<Daily> queryWrapper) {
        return getOne(queryWrapper);
    }
}
