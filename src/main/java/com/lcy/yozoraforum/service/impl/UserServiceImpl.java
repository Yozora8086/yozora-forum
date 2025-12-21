package com.lcy.yozoraforum.service.impl;

import ch.qos.logback.core.util.MD5Util;
import com.lcy.yozoraforum.dto.UserDTO;
import com.lcy.yozoraforum.dto.UserRegisterDTO;
import com.lcy.yozoraforum.entity.User;
import com.lcy.yozoraforum.exception.LoginErrorException;
import com.lcy.yozoraforum.exception.RegisterArgsErrorException;
import com.lcy.yozoraforum.mapper.UserMapper;
import com.lcy.yozoraforum.service.UserService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userDTO
     * @return
     */
    @Override
    public User login(UserDTO userDTO) {
        //获取登录所需的用户名和密码
        String userName = userDTO.getUserName();
        String userPassword = DigestUtils.md5DigestAsHex(userDTO.getUserPassword().getBytes());
        //查询数据库中匹配的数据
        User user = userMapper.selectUser(userName,userPassword);

        //登录基本信息返回给前端
        if (user == null){
            throw new LoginErrorException("用户名或密码错误");
        }
        return user;
    }

    /**
     * 用户注册
     * @param userRegisterDTO
     */
    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        //判断用户名是否符合命名规则
        if (userRegisterDTO.getUserName() == null || userRegisterDTO.getUserName().isEmpty() || userRegisterDTO.getUserName().length() > 20  ){
            throw new RegisterArgsErrorException("用户名不合法");
        }
        //判断密码是否符合规则
        if (userRegisterDTO.getUserPassword() == null || userRegisterDTO.getUserPassword().isEmpty() || (userRegisterDTO.getUserPassword().length() > 20 || userRegisterDTO.getUserPassword().length() < 6)){
            throw new RegisterArgsErrorException("密码不合法");
        }
        //将密码使用md5加密
        userRegisterDTO.setUserPassword(DigestUtils.md5DigestAsHex(userRegisterDTO.getUserPassword().getBytes()));
        //将DTO数据拷贝到User对象中
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO,user);
        //用户初始默认等级为1
        user.setUserLevel(1);
        //设置用户注册时间
        user.setRegisterTime(LocalDateTime.now());
        System.out.println(user);

        userMapper.insertUser(user);


    }
}
