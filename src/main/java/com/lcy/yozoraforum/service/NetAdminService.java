package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.SuperNotificationDTO;

public interface NetAdminService {
    /**
     * 发送系统通知信息
     * @param superNotificationDTO
     */
    void sendAllUserNotification(SuperNotificationDTO superNotificationDTO);
}
