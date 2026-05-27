package com.lcy.yozoraforum.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCount {
    //普通通知未读数量
    private Integer notificationCount;
    //系统通知未读数量
    private Integer superNotification;
}
