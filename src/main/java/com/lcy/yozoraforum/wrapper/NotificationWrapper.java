package com.lcy.yozoraforum.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationWrapper {
    //通知信息id
    private Long notificationId;
    //收到通知用户id
    private Long userId;
    //触发者（发起者，其他用户或者系统）
    private Long senderId;
    //通知类型 如评论 = 1，点赞 = 2，系统 = 3，私信 = 4
    private int type;
    //被操作对象类型 如帖子 = 1，资源 = 2，评论 =3
    private int target;
    //通知内容
//    private String content;
    //通知跳转url
    private String linkUrl;
    //是否已读
    private boolean isRead;
    //通知创建时间
    private LocalDateTime createTime;
    //发送者呢称
    private String senderName;
    //通知标题
    private String title;
}
