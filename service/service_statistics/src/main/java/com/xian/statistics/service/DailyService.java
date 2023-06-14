package com.xian.statistics.service;

import com.xian.entities.R;
import com.xian.statistics.entities.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author xian
 * @since 2022/06/28 23:35
 */
public interface DailyService extends IService<Daily> {

    R countRegister(String day);

    R countLogin(String day);

    R countCourse(String day);

    R countVideo(String day);

    R getStatistics(String day);

    R createTodayStatistics();

    Map<String, Object> getChartData(String begin, String end, String type);
}
