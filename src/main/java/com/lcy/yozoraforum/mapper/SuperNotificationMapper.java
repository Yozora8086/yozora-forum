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
    @Select("select user_level,content,create_time from super_notification where user_level = #{userlevel}")
    List<SuperNotificationVO> selectSuperNotification(int userLevel);
}
