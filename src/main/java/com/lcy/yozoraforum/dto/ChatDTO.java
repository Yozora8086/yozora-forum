package com.lcy.yozoraforum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    //聊天接收者Id
    private Long receiverId;
    //聊天内容
    private String content;
}
