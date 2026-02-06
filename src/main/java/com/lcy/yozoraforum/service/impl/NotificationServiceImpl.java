package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.entity.Notification;
import com.lcy.yozoraforum.mapper.NotificationMapper;
import com.lcy.yozoraforum.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 查询用户收到的通知
     * @return
     */
    @Override
    public List<Notification> selectMsg() {
        //获取当前登录用户通知集合
        List<Notification> notificationsList = notificationMapper.selectMsg(BaseContext.getCurrentId());
        return notificationsList;
    }
}
