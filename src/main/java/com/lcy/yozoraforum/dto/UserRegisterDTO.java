package com.lcy.yozoraforum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    //用户昵称
    private String userName;
    //用户邮箱
    private String userEmail;
    //用户密码
    private String userPassword;
    //用户年龄
    private int userAge;
}
