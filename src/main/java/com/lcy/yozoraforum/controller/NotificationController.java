package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.entity.Notification;
import com.lcy.yozoraforum.service.NotificationService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.NotificationVO;
import com.lcy.yozoraforum.vo.SuperNotificationVO;
import com.lcy.yozoraforum.wrapper.NotificationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/yozora/notification")

public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    /**
     * 查询用户收到的普通通知
     * @return
     */
    @GetMapping("/msg")
    public Result<List<NotificationVO>> selectNotifications(){
        //获取该用户通知集合
        List<NotificationWrapper> notificationList = notificationService.selectMsg();

        //将notification实体对象转换成notificationVO对象
        List<NotificationVO> notificationVOList = notificationList.stream()
                .map(notification -> {
                    NotificationVO notificationVO = new NotificationVO();
                    notificationVO.setNotificationId(notification.getNotificationId());
                    notificationVO.setUserId(notification.getUserId());
                    notificationVO.setSenderId(notification.getSenderId());
                    notificationVO.setType(notification.getType());
                    notificationVO.setTarget(notification.getTarget());
//                    notificationVO.setContent(notification.getContent());
                    notificationVO.setTitle(notification.getTitle());
                    notificationVO.setLinkUrl(notification.getLinkUrl());
                    notificationVO.setRead(notification.isRead());
                    notificationVO.setCreateTime(notification.getCreateTime());
                    notificationVO.setSenderName(notification.getSenderName());
                    return notificationVO;
                })
                .collect(Collectors.toList());


        return Result.success(notificationVOList);
    }

    /**
     * 查询用户收到的系统通知
     * @return
     */
    @GetMapping("/superNotification")
    public Result<List<SuperNotificationVO>> selectSuperNotification(){
        List<SuperNotificationVO> superNotificationList = notificationService.selectSuperNotification();
        return Result.success(superNotificationList);
    }

    /**
     * 浏览通知详情
     * @param notificationId
     * @return
     */
    @GetMapping("/readNotification")
    public Result<NotificationVO> readNotification(@RequestParam Long notificationId){
        NotificationVO notificationVO = notificationService.readNotification(notificationId);
        return Result.success(notificationVO);
    }

    /**
     * 浏览系统通知详情
     * @param notificationId
     * @return
     */
    @GetMapping("/readSuperNotification")
    public Result<SuperNotificationVO> readSuperNotification(@RequestParam Long notificationId){
        SuperNotificationVO superNotificationVO = notificationService.readSuperNotification(notificationId);
        return Result.success(superNotificationVO);
    }


    /**
     * 统计未读通知数量
     * @return
     */
    @GetMapping("/getAllNotificationNum")
    public Result<Map<String,Integer>> getAllNotificationNum(){
        Map<String,Integer> countMap = notificationService.getAllNotificationNum();
        return Result.success(countMap);
    }

}
