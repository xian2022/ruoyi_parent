package com.xian.serviceorder.rabbitmq.listener;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rabbitmq.client.Channel;
import com.xian.serviceorder.entities.Order;
import com.xian.serviceorder.rabbitmq.utils.MQConstant;
import com.xian.serviceorder.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @description: 订单失效监听器
 * @author: nxq email: niuxiangqian163@163.com
 * @createDate: 2020/12/18 8:30 上午
 * @updateUser: nxq email: niuxiangqian163@163.com
 * @updateDate: 2020/12/18 8:30 上午
 * @updateRemark:
 * @version: 1.0
 **/
@Component
@Slf4j
public class OrderFailureListener {
    @Autowired
    private OrderService orderService;
    @RabbitListener(queues = MQConstant.DEAL_QUEUE_ORDER)//设置订单失效的队列
    public void process(String orderNo, Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("order_no", orderNo));
        if (ObjectUtil.isNotNull(order)) {
            if (order.getStatus()!=1) {
                log.info("【订单号】 - [{}] 已失效", orderNo);
                log.info("【message】 - [{}]", message);
                log.info("【headers】 - [{}]", headers);
                log.info("【channel】 - [{}]", channel);
                orderService.update(new UpdateWrapper<Order>().eq("order_no", orderNo).set("status", -2));
                System.out.println("执行结束....");
            }
        }
    }
}
