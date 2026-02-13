package com.lcy.yozoraforum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateMsgDTO {
    //用户昵称
    private String userName;
    //用户年龄
    private Integer userAge;
    //用户个人简介
    private String userMessage;
}
