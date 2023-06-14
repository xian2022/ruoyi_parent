package com.xian.serviceorder.rabbitmq.utils;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class BindingUtil {
    @Qualifier("orderExchange")
    @Autowired
    private DirectExchange directExchange;
    @Qualifier("deadExchangeOrder")
    @Autowired
    private DirectExchange deadExchangeOrder;
    @Resource
    private RabbitAdmin rabbitAdmin;

    public void createOrderQueueAndBinding(String id) {
        // 将普通队列绑定到死信队列交换机上
        Map<String, Object> args = new HashMap<>(2);
        //args.put("x-message-ttl", 1000 * 5);//直接设置 Queue 延迟时间 但如果直接给队列设置过期时间,这种做法不是很灵活
        //这里采用发送消息动态设置延迟时间,这样我们可以灵活控制
        args.put(MQConstant.DEAD_LETTER_QUEUE_KEY, MQConstant.DEAL_EXCHANGE_ORDER);
        args.put(MQConstant.DEAD_LETTER_ROUTING_KEY, MQConstant.DEAD_ROUTING_KEY_ORDER);
        Queue queue = new Queue(MQConstant.ORDER_QUEUE + id, false, false, true, args);
        // 创建Queue
        rabbitAdmin.declareQueue(queue);
        // 绑定Queue队列到交换机,并且指定routingKey
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(MQConstant.ROUTING_KEY_ORDER+id));
    }
}
