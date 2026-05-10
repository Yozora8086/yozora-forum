package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.UserUpdateMsgDTO;
import com.lcy.yozoraforum.exception.RegisterArgsErrorException;
import com.lcy.yozoraforum.mapper.UserMapper;
import com.lcy.yozoraforum.service.UserUpdateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateMessageServiceImpl implements UserUpdateMessageService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户修改个人信息
     * @param updateMsgDTO
     */
    @Override
    public void update(UserUpdateMsgDTO updateMsgDTO) {

        //判断用户名是否符合命名规则
        if (updateMsgDTO.getUserName() != null){
            if (updateMsgDTO.getUserName() == null || updateMsgDTO.getUserName().isEmpty() || updateMsgDTO.getUserName().length() > 20  ){
                throw new RegisterArgsErrorException("用户名不合法");
            }
        }

        //判断该用户名和邮箱是否已经被使用
        int rows = userMapper.isExistUserName(updateMsgDTO.getUserName());
        if (rows != 0){
            throw new RegisterArgsErrorException("邮箱/用户名已被使用");
        }

        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        //修改数据库
        userMapper.update(updateMsgDTO,userId);
    }
}
