package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.entity.Notification;
import com.lcy.yozoraforum.vo.SuperNotificationVO;
import com.lcy.yozoraforum.wrapper.NotificationWrapper;

import java.util.List;

public interface NotificationService {
    /**
     * 查询用户收到的通知
     * @return
     */
    List<NotificationWrapper> selectMsg();

    /**
     * 查询用户收到的系统通知
     * @return
     */
    List<SuperNotificationVO> selectSuperNotification();
}
