package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.dto.UserDTO;
import com.lcy.yozoraforum.dto.UserRegisterDTO;
import com.lcy.yozoraforum.entity.User;
import com.lcy.yozoraforum.service.UserService;
import com.lcy.yozoraforum.util.JWTUtils;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("yozora/user")
/**
 * 用户操作
 */
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserVo> login(@RequestBody UserDTO userDTO){
       User user = userService.login(userDTO);

       //生成Jwt令牌
       String token = JWTUtils.createToken(user.getUserId());
        System.out.println("令牌是:"+token);
        UserVo userVo = new UserVo();


        userVo.setUserId(user.getUserId());
        userVo.setUserName(user.getUserName());
        userVo.setToken(token);
       return Result.success(userVo);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO){
        userService.register(userRegisterDTO);
        return Result.success("注册成功");

    }
}
