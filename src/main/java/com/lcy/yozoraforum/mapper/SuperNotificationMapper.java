package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.SuperNotificationDTO;
import com.lcy.yozoraforum.vo.SuperNotificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SuperNotificationMapper {
    /**
     * 将系统通知信息插入数据库
     * @param superNotificationDTO
     */
    void insertMsg(SuperNotificationDTO superNotificationDTO);

    /**
     * 查询系统通知
     * @param userLevel
     * @return
     */
    @Select("select super_notification_id,user_level,title,content,create_time from super_notification where user_level = #{userlevel}")
    List<SuperNotificationVO> selectSuperNotification(int userLevel);


    /**
     * 查询系统通知详情
     * @param notificationId
     * @return
     */
    SuperNotificationVO selectContent(Long notificationId);

    /**
     * 获取系统未读通知数量（反查）
     * @param userId
     * @return
     */
    Integer getSystemNotification(Long userId);

    /**
     * 获取系统已读通知数量(正查)
     */
    @Select("select count(*) from notification where user_id = #{userId} and type = 3 and is_read = 1")
    Integer getSystemNotification2(Long userId);

    /**
     * 获取普通用户级别权限的所有系统通知数量
     * @return
     */
    @Select("select count(*) from super_notification where user_level = 3")
    Integer getAllUserSuperNotificationCount();

    /**
     * 获取管理员级别权限的所有系统通知数量
     * @return
     */
    @Select("select count(*) from super_notification")
    Integer getAllAdminSuperNotificationCount();
}
