package com.lcy.yozoraforum.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowUserMsgVO {
    //用户昵称
    private String userName;
    //用户邮箱
    private String userEmail;
    //用户个人简介
    private String userMessage;
    //用户注册时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;
}
