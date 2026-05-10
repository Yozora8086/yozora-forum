package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.entity.Chat;
import com.lcy.yozoraforum.vo.ChatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {
    /**
     * 插入聊天数据
     * @param chat
     */
     void insert(Chat chat);

    /**
     * 查询聊天数据
     * @param senderId
     * @param receiverId
     */
    List<ChatVO> select(@Param("senderId") Long senderId, @Param("receiverId") String receiverId);
}
