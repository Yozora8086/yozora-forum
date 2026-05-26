package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.entity.Notification;
import com.lcy.yozoraforum.mapper.NotificationMapper;
import com.lcy.yozoraforum.mapper.SuperNotificationMapper;
import com.lcy.yozoraforum.mapper.UserMapper;
import com.lcy.yozoraforum.service.NotificationService;
import com.lcy.yozoraforum.vo.SuperNotificationVO;
import com.lcy.yozoraforum.wrapper.NotificationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private SuperNotificationMapper superNotificationMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户收到的通知
     * @return
     */
    @Override
    public List<NotificationWrapper> selectMsg() {
        //获取当前登录用户通知集合
        List<NotificationWrapper> notificationsList = notificationMapper.selectMsg(BaseContext.getCurrentId());

        for (NotificationWrapper notificationWrapper : notificationsList) {
            System.out.println(notificationWrapper);
        }
        return notificationsList;
    }

    /**
     * 查询用户收到的系统通知
     * @return
     */
    @Override
    public List<SuperNotificationVO> selectSuperNotification() {
        Long userId = BaseContext.getCurrentId();
        int userLevel = userMapper.selectUserLevel(userId);
        List<SuperNotificationVO> superNotificationVOList = superNotificationMapper.selectSuperNotification(userLevel);
        return superNotificationVOList;
    }
}
