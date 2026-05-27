package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.entity.Notification;
import com.lcy.yozoraforum.vo.NotificationVO;
import com.lcy.yozoraforum.vo.SuperNotificationVO;
import com.lcy.yozoraforum.wrapper.NotificationWrapper;

import java.util.List;
import java.util.Map;

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

    /**
     * 浏览通知详情
     * @param notificationId
     * @return
     */
    NotificationVO readNotification(Long notificationId);

    /**
     * 浏览系统通知详情
     * @param notificationId
     * @return
     */
    SuperNotificationVO readSuperNotification(Long notificationId);

    /**
     * 统计未读通知数量
     * @return
     */
    Map<String,Integer> getAllNotificationNum();
}
