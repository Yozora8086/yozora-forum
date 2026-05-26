package com.lcy.yozoraforum.service.impl;

import com.alibaba.fastjson.JSON;
import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.SuperNotificationDTO;
import com.lcy.yozoraforum.exception.PermissionException;
import com.lcy.yozoraforum.mapper.SuperNotificationMapper;
import com.lcy.yozoraforum.mapper.UserMapper;
import com.lcy.yozoraforum.service.NetAdminService;
import com.lcy.yozoraforum.wrapper.SuperNotificationWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NetAdminServiceImpl implements NetAdminService {

    @Autowired
    private SuperNotificationMapper superNotificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送系统通知信息
     * @param superNotificationDTO
     */
    @Override
    public void sendAllUserNotification(SuperNotificationDTO superNotificationDTO) {
        //获取当前操作用户的权限等级
        Long userId = BaseContext.getCurrentId();
        int userLevel = userMapper.selectUserLevel(userId);
        //判断用户是否为最高权限
        if (userLevel != 1 ){
            //不是就抛越权异常
           throw new PermissionException();
        }

        //设置时间戳
        superNotificationDTO.setCreateTime(LocalDateTime.now());
        //将系统通知信息插入数据库
        superNotificationMapper.insertMsg(superNotificationDTO);

        SuperNotificationWrapper superNotificationWrapper = SuperNotificationWrapper.builder()
                .userId(BaseContext.getCurrentId())
                .content(superNotificationDTO.getContent())
                .createTime(superNotificationDTO.getCreateTime())
                .userLevel(superNotificationDTO.getUserLevel())
                .build();

        //查询通知信息

        rabbitTemplate.convertAndSend(
                "system.notification.direct",
                "systemNotification",
                JSON.toJSONString(superNotificationWrapper)
        );

    }
}
