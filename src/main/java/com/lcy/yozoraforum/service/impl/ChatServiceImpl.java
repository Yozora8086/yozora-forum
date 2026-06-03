package com.lcy.yozoraforum.service.impl;

import com.alibaba.fastjson.JSON;
import com.lcy.yozoraforum.constant.RedisConstants;
import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.entity.Chat;
import com.lcy.yozoraforum.entity.Notification;
import com.lcy.yozoraforum.handler.NotifyWebSocketHandler;
import com.lcy.yozoraforum.mapper.ChatMapper;
import com.lcy.yozoraforum.mapper.NotificationMapper;
import com.lcy.yozoraforum.service.ChatService;
import com.lcy.yozoraforum.util.NotificationPush;
import com.lcy.yozoraforum.vo.ChatVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 私信
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private RedisTemplate redisTemplate;

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

        //通知插入数据库前的包装
        Notification notification = NotificationPush.Add(receiverId, 4, null, chat.getChatContent(),"");

        //插入数据库
        notificationMapper.InsertNotification(notification);
        //将聊天信息写入数据库
        chatMapper.insert(chat);

        //redis执行lua脚本通知：通知自增
        redisTemplate.opsForValue().increment(RedisConstants.NOTIFICATION_COUNT_KEY + receiverId,1);

        //发MQ
        rabbitTemplate.convertAndSend(
                "chat.direct",
                "chatPrivate",
                JSON.toJSONString(chat)
        );

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
