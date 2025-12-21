package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //用户ID
    private Integer userId;
    //用户昵称
    private String userName;
    //用户邮箱
    private String userEmail;
    //用户密码
    private String userPassword;
    //用户等级权限
    private Integer userLevel;
    //用户年龄
    private Integer userAge;
    //用户个人简介
    private String userMessage;
    //用户注册时间
    private LocalDateTime registerTime;

}
