package com.xian.serviceorder.mapper;

import com.xian.serviceorder.entities.PayLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 支付日志表 Mapper 接口
 * </p>
 *
 * @author xian
 * @since 2022/06/26 23:32
 */
@Mapper
public interface PayLogMapper extends BaseMapper<PayLog> {

}
