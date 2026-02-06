package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.entity.Notification;
import com.lcy.yozoraforum.service.NotificationService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.NotificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/yozora/notification")

public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    /**
     * 查询用户收到的通知
     * @return
     */
    @GetMapping("/msg")
    public Result<List<NotificationVO>> selectNotifications(){
        //获取该用户通知集合
        List<Notification> notificationList = notificationService.selectMsg();

        //将notification实体对象转换成notificationVO对象
        List<NotificationVO> notificationVOList = notificationList.stream()
                .map(notification -> {
                    NotificationVO notificationVO = new NotificationVO();
                    notificationVO.setUserId(notification.getUserId());
                    notificationVO.setSenderId(notification.getSenderId());
                    notificationVO.setType(notification.getType());
                    notificationVO.setTarget(notification.getTarget());
                    notificationVO.setContent(notification.getContent());
                    notificationVO.setLinkUrl(notification.getLinkUrl());
                    notificationVO.setRead(notification.isRead());
                    notificationVO.setCreateTime(notification.getCreateTime());
                    return notificationVO;
                })
                .collect(Collectors.toList());


        return Result.success(notificationVOList);
    }
}
