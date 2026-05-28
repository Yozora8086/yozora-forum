package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.entity.Notification;
import com.lcy.yozoraforum.mapper.NotificationMapper;
import com.lcy.yozoraforum.mapper.SuperNotificationMapper;
import com.lcy.yozoraforum.mapper.UserMapper;
import com.lcy.yozoraforum.service.NotificationService;
import com.lcy.yozoraforum.vo.NotificationVO;
import com.lcy.yozoraforum.vo.SuperNotificationVO;
import com.lcy.yozoraforum.wrapper.NotificationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 浏览通知详情
     * @param notificationId
     * @return
     */
    @Override
    public NotificationVO readNotification(Long notificationId) {
        //浏览通知
        NotificationVO notificationVO = notificationMapper.selectContent(notificationId);
        //如果已读则不用修改数据库的is_read字段
        if (notificationVO.isRead() == true){
            return notificationVO;
        }

        notificationMapper.contentRead(notificationId);
        return notificationVO;
    }

    /**
     * 浏览系统通知详情
     * @param notificationId
     * @return
     */
    @Override
    public SuperNotificationVO readSuperNotification(Long notificationId) {
        //查询系统通知
        SuperNotificationVO superNotificationVO = superNotificationMapper.selectContent(notificationId);

        //将系统通知Vo赋给系统通知实体类
        Notification notification = Notification.builder()
                .userId(BaseContext.getCurrentId())
                .type(3)
                .title(superNotificationVO.getTitle())
                .superNotificationId(superNotificationVO.getSuperNotificationId())
                .content(superNotificationVO.getContent())
                .createTime(LocalDateTime.now())
                .isRead(true)
                .build();

        //获取当前用户ID
        Long userId = BaseContext.getCurrentId();

        //获取当前系统通知是否已经写入数据库
        Integer num = notificationMapper.getSuperNotificationIsRead(superNotificationVO.getSuperNotificationId(),userId);

        //如果已读则不用修改数据库的is_read字段
        if (num == 1){
            System.out.println("不再执行");
            return superNotificationVO;
        }

        //将系统通知插入通知里
        notificationMapper.InsertNotification(notification);

        return superNotificationVO;
    }

    /**
     * 统计未读通知数量
     * @return
     */
    @Override
    public Map<String,Integer> getAllNotificationNum() {
        Long userId= BaseContext.getCurrentId();
        //查询普通通知未读数量
        Integer notificationCount = notificationMapper.getNotification(userId);

        //查询系统通知未读数量
        Integer superNotification = superNotificationMapper.getSystemNotification(userId);

        //创建map集合
        Map<String,Integer> countMap = new HashMap<>();
        //将各项未读通知加入map集合
        countMap.put("notificationCount",notificationCount);
        countMap.put("superNotification",superNotification);
        countMap.put("allNotification",notificationCount + superNotification);

        return countMap;
    }
}
