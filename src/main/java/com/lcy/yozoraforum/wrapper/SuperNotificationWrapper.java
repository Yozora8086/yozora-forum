package com.lcy.yozoraforum.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuperNotificationWrapper {
    //推送者id
    private Long userId;
    //通知标题
    private String title;
    //通知内容
    private String content;
    //接收用户类型
    private Integer userLevel;
    //通知创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
