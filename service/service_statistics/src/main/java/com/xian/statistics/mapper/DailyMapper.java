package com.xian.statistics.mapper;

import com.xian.statistics.entities.Daily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 网站统计日数据 Mapper 接口
 * </p>
 *
 * @author xian
 * @since 2022/06/28 23:35
 */
@Mapper
public interface DailyMapper extends BaseMapper<Daily> {

}
