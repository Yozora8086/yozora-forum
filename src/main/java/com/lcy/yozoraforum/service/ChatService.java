package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.vo.ChatVO;

import java.util.List;

public interface ChatService {
    /**
     * 发送私信
     * @param receiverId
     * @param content
     */
    void sendMessage(Long receiverId,String content);

    /**
     * 获取聊天记录
     * @param senderId
     * @param receiverId
     */
    List<ChatVO> select(Long senderId, String receiverId);
}

