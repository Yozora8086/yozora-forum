package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.UserUpdateMsgDTO;

public interface UserUpdateMessageService {
    /**
     * 用户修改个人信息
     */
    void update(UserUpdateMsgDTO updateMsgDTO);
}
