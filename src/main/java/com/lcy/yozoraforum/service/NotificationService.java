package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.entity.Notification;

import java.util.List;

public interface NotificationService {
    /**
     * 查询用户收到的通知
     * @return
     */
    List<Notification> selectMsg();
}
