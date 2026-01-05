package com.lcy.yozoraforum.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    //用户ID
    private Long userId;
    //用户昵称
    private String userName;
    //令牌token
    private String token;
}
