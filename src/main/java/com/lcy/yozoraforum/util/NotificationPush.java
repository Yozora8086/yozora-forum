package com.lcy.yozoraforum.util;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.entity.Notification;

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
    public static Notification Add(Long userId,int type,int target,String content,String url){
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
}
