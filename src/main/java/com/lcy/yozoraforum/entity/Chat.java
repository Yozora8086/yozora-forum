package com.lcy.yozoraforum.entity;

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
public class Chat {
    //聊天信息Id
    private Long id;
    //聊天推送者Id
    private Long chatSenderId;
    //聊天接收者Id
    private Long chatReceiverId;
    //聊天内容
    private String chatContent;
    //聊天信息  发送/接受  时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    //聊天信息是否已读
    private int chatIsread;
 }
