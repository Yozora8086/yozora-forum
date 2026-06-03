package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.UserDTO;
import com.lcy.yozoraforum.dto.UserUpdateMsgDTO;
import com.lcy.yozoraforum.entity.User;
import com.lcy.yozoraforum.vo.ShowUserMsgVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 判断该用户名和邮箱是否已经被使用
     * @param userName
     * @param userEmail
     * @return
     */
    @Select("select count(*) from user where user_name = #{userName} or user_email = #{userEmail}")
    int isExist(String userName, String userEmail);

    /**
     * 判断该用户名是否已经被使用(用户修改个人信息时判断)
     * @param userName
     * @return
     */
    @Select("select count(*) from user where user_name = #{userName} or user_email = #{userEmail}")
    int isExistUserName(String userName);

    /**
     * 用户修改个人信息
     * @param updateMsgDTO
     */
    void update(@Param("updateMsgDTO") UserUpdateMsgDTO updateMsgDTO, @Param("userId") Long userId);


    /**
     * 查询用户自己的个人信息
     * @param userId
     * @return
     */
    ShowUserMsgVO selectUserMsg(Long userId);

    /**
     * 查询当前登录用户的用户等级
     * @param userId
     * @return
     */
    @Select("select user_level from user where user_id = #{userId}")
    int selectUserLevel(Long userId);

    /**
     * 查询所有权限等级为管理员级的用户id
     * @return
     */
    @Select("select user_id from user where user_level != 3")
    List<Long> selectAllAdminUser();
}
