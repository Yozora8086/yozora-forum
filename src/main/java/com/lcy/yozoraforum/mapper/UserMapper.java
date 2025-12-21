package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.UserDTO;
import com.lcy.yozoraforum.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 登录查询用户信息
     * @param
     * @return
     */
    @Select("select * from user where user_name = #{userName} and user_password = #{userPassword}")
    User selectUser(String userName,String userPassword);

    /**
     * 注册账号，插入用户数据信息
     * @param user
     */
    @Insert("insert into user (user_name,user_email,user_password,user_level,user_age,register_time)" +
            "values " +
            "(#{userName},#{userEmail},#{userPassword},#{userLevel},#{userAge},#{registerTime})")
    void insertUser(User user);
}
