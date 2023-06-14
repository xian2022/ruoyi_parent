package com.xian.serviceorder.mapper;

import com.xian.serviceorder.entities.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author xian
 * @since 2022/06/25 11:53
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
