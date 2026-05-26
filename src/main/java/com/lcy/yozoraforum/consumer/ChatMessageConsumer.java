package com.lcy.yozoraforum.consumer;

import com.alibaba.fastjson.JSON;
import com.lcy.yozoraforum.entity.Chat;
import com.lcy.yozoraforum.handler.NotifyWebSocketHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageConsumer {

    @RabbitListener(queues = "chat.private.queue")
    public void handleMessage(String msg){
        Chat chat = JSON.parseObject(msg, Chat.class);

        System.out.println("收到MQ消息：" + chat);
        //WebSocket推送

        NotifyWebSocketHandler.push(
                chat.getChatReceiverId(),
                chat.getChatContent()
        );
    }
}
