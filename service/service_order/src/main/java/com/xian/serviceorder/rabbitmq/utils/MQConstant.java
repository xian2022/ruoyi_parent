package com.xian.serviceorder.rabbitmq.utils;

public class MQConstant {
    //队列名称
    public static final String ORDER_QUEUE = "order_queue";

    //交换机名称
    public static final String ORDER_EXCHANGE = "order_exchange";

    // routingKey
    public static final String ROUTING_KEY_ORDER = "routing_key_order";

    //死信消息队列名称
    public static final String DEAL_QUEUE_ORDER = "deal_queue_order";

    //死信交换机名称
    public static final String DEAL_EXCHANGE_ORDER = "deal_exchange_order";

    //死信 routingKey
    public static final  String DEAD_ROUTING_KEY_ORDER = "dead_routing_key_order";

    //死信队列 交换机标识符
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";

    //死信队列交换机绑定键标识符
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
}
