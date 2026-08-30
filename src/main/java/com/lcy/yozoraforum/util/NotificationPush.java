package com.lcy.yozoraforum.util;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.entity.Chat;
import com.lcy.yozoraforum.entity.Notification;

import java.time.LocalDateTime;

/**
 * 通知/聊天相关方法
 */
public class NotificationPush {
    /**
     * 通知插入数据库前的包装
     * @param userId
     * @param type
     * @param target
     * @param content
     * @return
     */
    public static Notification Add(Long userId,int type,Integer target,String content,String url){
        /**
         * 构建 notification 对象
         */
        Notification notification = Notification.builder()
                .userId(userId)
                .senderId(BaseContext.getCurrentId())
                .type(type)
                .target(target)
                .content(content)
                .linkUrl(url)
                .isRead(false)
                .build();

        return notification;
    }

    /**
     * 将聊天插入数据库前的包装
     * @param senderId
     * @param receiverId
     * @param content
     * @return
     */
    public static Chat AddChat(Long senderId,Long receiverId,String content){
        /**
         * 构建 chat 对象
         */
        Chat chat = Chat.builder()
                .chatSenderId(senderId)
                .chatReceiverId(receiverId)
                .chatContent(content)
                .createTime(LocalDateTime.now())
                .chatIsread(0)
                .build();

        return chat;
    }
}
