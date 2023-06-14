package com.xian.serviceorder.service.impl;

import com.xian.serviceorder.entities.Order;
import com.xian.serviceorder.mapper.OrderMapper;
import com.xian.serviceorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/06/25 11:53
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
