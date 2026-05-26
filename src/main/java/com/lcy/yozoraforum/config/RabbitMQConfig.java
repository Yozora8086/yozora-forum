package com.lcy.yozoraforum.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //聊天交换机
    private static final String CHAT_EXCHANGE = "chat.direct";
    //私聊队列
    private static final String CHAT_QUEUE = "chat.private.queue";
    //私聊key
    private static final String CHAT_KEY = "chatPrivate";


    //系统消息交换机
    private static final String SYSTEM_NOTIFICATION_EXCHANGE = "system.notification.direct";
    //系统消息队列
    private static final String SYSTEM_NOTIFICATION_QUEUE = "system.notification.queue";
    //系统消息 key
    private static final String SYSTEM_NOTIFICATION_KEY = "systemNotification";

    //创建交换机
    @Bean
    public DirectExchange chatExchange(){
        return new DirectExchange(CHAT_EXCHANGE);
    }

    //创建队列
    @Bean
    public Queue chatQueue(){
        return new Queue(CHAT_QUEUE);
    }

    //交换机绑定队列
    @Bean
    public Binding chatBinging(){
        return BindingBuilder
                .bind(chatQueue())
                .to(chatExchange())
                .with(CHAT_KEY);

    }

    //创建交换机
    @Bean
    public DirectExchange systemNotificationExchange(){
        return new DirectExchange(SYSTEM_NOTIFICATION_EXCHANGE);
    }

    //创建队列
    @Bean
    public Queue systemNotificationQueue(){
        return new Queue(SYSTEM_NOTIFICATION_QUEUE);
    }

    //交换机绑定队列
    @Bean
    public Binding systemNotificationBinging(){
        return BindingBuilder
                .bind(systemNotificationQueue())
                .to(systemNotificationExchange())
                .with(SYSTEM_NOTIFICATION_KEY);

    }
}
