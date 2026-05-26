package com.lcy.yozoraforum.consumer;

import com.alibaba.fastjson.JSON;
import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.SuperNotificationDTO;
import com.lcy.yozoraforum.handler.NotifyWebSocketHandler;
import com.lcy.yozoraforum.wrapper.SuperNotificationWrapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SuperNotificationConsumer {
    @RabbitListener(queues = "system.notification.queue")
    public void handleSuperNotification(String msg){
        SuperNotificationWrapper superNotificationWrapper = JSON.parseObject(msg, SuperNotificationWrapper.class);

        NotifyWebSocketHandler.push(
                superNotificationWrapper.getUserId(),
                msg
                );
    }
}
