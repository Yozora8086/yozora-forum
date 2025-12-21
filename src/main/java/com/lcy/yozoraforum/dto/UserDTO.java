package com.lcy.yozoraforum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    //用户昵称
    private String userName;
    //用户密码
    private String userPassword;
}
