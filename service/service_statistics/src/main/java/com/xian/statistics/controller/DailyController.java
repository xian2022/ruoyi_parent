package com.xian.statistics.controller;

import com.xian.entities.R;
import com.xian.statistics.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/06/28 23:35
 */
@CrossOrigin
@RestController
@RequestMapping("/statistics/sta")
public class DailyController {
    @Autowired
    private DailyService dailyService;
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable("day") String day) {
        return dailyService.countRegister(day);
    }
    @GetMapping("/countLogin/{day}")
    public R countLogin(@PathVariable("day") String day) {
        return dailyService.countLogin(day);
    }
    @GetMapping("/countCourse/{day}")
    public R countCourse(@PathVariable("day") String day) {
        return dailyService.countCourse(day);
    }
    @GetMapping("/countVideo/{day}")
    public R countVideo(@PathVariable("day") String day) {
        return dailyService.countVideo(day);
    }
    @GetMapping("/getStatistics/{day}")
    public R getStatistics(@PathVariable("day") String day) {
        return dailyService.getStatistics(day);
    }
    @GetMapping("/getStatistics")
    public R createTodayStatistics() {
        return dailyService.createTodayStatistics();
    }
    @GetMapping("show-chart/{begin}/{end}/{type}")
    public R showChart(@PathVariable("begin") String begin,@PathVariable("end") String end,@PathVariable("type") String type){
        Map<String, Object> map = dailyService.getChartData(begin, end, type);
        return R.ok().data(map);
    }

}
