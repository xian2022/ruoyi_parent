package com.xian.serviceorder.config.rabbitmq;

import com.xian.serviceorder.rabbitmq.utils.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadLetterMQConfig {
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    //声明一个direct类型的交换机
    @Bean
    DirectExchange orderExchange() {
        return new DirectExchange(MQConstant.ORDER_EXCHANGE);
    }

    //创建配置死信队列
    @Bean
    public Queue deadQueueOrder() {
        Queue queue = new Queue(MQConstant.DEAL_QUEUE_ORDER, true);
        return queue;
    }
    //创建死信交换机
    @Bean
    public DirectExchange deadExchangeOrder() {
        return new DirectExchange(MQConstant.DEAL_EXCHANGE_ORDER);
    }

    //死信队列与死信交换机绑定
    @Bean
    public Binding bindingDeadExchange() {
        return BindingBuilder.bind(deadQueueOrder()).to(deadExchangeOrder()).with(MQConstant.DEAD_ROUTING_KEY_ORDER);
    }
}

