package com.lcy.yozoraforum.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperNotification {
    //通知信息id
    private Long superNotificationId;
    //接收用户类型
    private Integer userLevel;
    //消息内容
    private String content;
    //通知创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
