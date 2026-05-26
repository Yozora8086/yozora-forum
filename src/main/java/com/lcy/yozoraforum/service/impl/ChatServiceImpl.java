package com.lcy.yozoraforum.service.impl;

import com.alibaba.fastjson.JSON;
import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.entity.Chat;
import com.lcy.yozoraforum.handler.NotifyWebSocketHandler;
import com.lcy.yozoraforum.mapper.ChatMapper;
import com.lcy.yozoraforum.service.ChatService;
import com.lcy.yozoraforum.util.NotificationPush;
import com.lcy.yozoraforum.vo.ChatVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 私信
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 当前用户发送私信
     * @param receiverId
     * @param content
     */
    @Override
    public void sendMessage(Long receiverId, String content) {
        //获取当前用户id
        Long senderId = BaseContext.getCurrentId();

        Chat chat = NotificationPush.AddChat(senderId,receiverId,content);

        //将聊天信息写入数据库
        chatMapper.insert(chat);

        System.out.println("准备发送MQ");

        //发MQ
        rabbitTemplate.convertAndSend(
                "chat.direct",
                "chatPrivate",
                JSON.toJSONString(chat)
        );

        System.out.println("MQ发送完成");

    }

    /**
     * 获取聊天记录
     * @param senderId
     * @param receiverId
     * @return
     */
    @Override
    public List<ChatVO> select(Long senderId, String receiverId) {
        //查询数据库
        List<ChatVO> chatList = chatMapper.select(senderId, receiverId);

        return chatList;
    }
}
