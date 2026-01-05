package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Notification {
    //通知信息id
    private Long notificationId;
    //收到通知用户id
    private Long userId;
    //触发者（发起者，其他用户或者系统）
    private Long senderId;
    //通知类型 如评论，点赞，系统
    private String type;
    //被操作对象类型 如帖子，资源，评论
    private String target;
    //通知内容
    private String content;
    //通知跳转url
    private String linkUrl;
    //是否已读
    private boolean isRead;
    //通知创建时间
    private LocalDateTime createTime;

}
