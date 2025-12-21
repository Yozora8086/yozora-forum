package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.UserDTO;
import com.lcy.yozoraforum.dto.UserRegisterDTO;
import com.lcy.yozoraforum.entity.User;
import com.lcy.yozoraforum.vo.UserVo;

public interface UserService {
    /**
     * 用户登录
     * @param userDTO
     * @return
     */
    User login(UserDTO userDTO);

    /**
     * 用户注册
     * @param userRegisterDTO
     */
    void register(UserRegisterDTO userRegisterDTO);
}
