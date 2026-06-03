package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.constant.RedisConstants;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private SuperNotificationMapper superNotificationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private DefaultRedisScript userNotificationScript;

    @Autowired
    private DefaultRedisScript adminNotificationScript;


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
        Long userId = BaseContext.getCurrentId();
        //浏览通知
        NotificationVO notificationVO = notificationMapper.selectContent(notificationId);
        //如果已读则不用修改数据库的is_read字段
        if (notificationVO.isRead() == true){
            return notificationVO;
        }

        notificationMapper.contentRead(notificationId);


        //redis执行lua脚本通知：通知自减
        redisTemplate.opsForValue().decrement("notification:count:" + userId,1);

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
        //获取当前用户权限等级
        Integer userLevel = BaseContext.getCurrentLevel();

        if (userLevel == 3) {
            //redis执行lua脚本通知：系统通知自减
            redisTemplate.opsForValue().increment("superNotification:count:" + userId,1);
        }

        if (userLevel == 2 || userLevel == 1) {
            //redis执行lua脚本通知：管理员系统通知自减
            redisTemplate.opsForValue().increment("superNotification:admin:count:" + userId,1);
        }

        return superNotificationVO;
    }

    /**
     * 统计未读通知数量
     * @return
     */
    @Override
    public Map<String,Integer> getAllNotificationNum() {
        //获取当前用户id
        Long userId= BaseContext.getCurrentId();
        //获取当前用户权限等级
        Integer userLevel = BaseContext.getCurrentLevel();
        //获取缓存的通知数量
        Object notificationObj = redisTemplate.opsForValue().get(RedisConstants.NOTIFICATION_COUNT_KEY + userId);
        Object superNotificationObj = null;
        if ( userLevel == 3) {
            //获取缓存的系统通知数量
            superNotificationObj = redisTemplate.opsForValue().get(RedisConstants.SUPER_NOTIFICATION_COUNT_KEY + userId);
        } else {
            superNotificationObj = redisTemplate.opsForValue().get(RedisConstants.ADMIN_SUPER_NOTIFICATION_COUNT_KEY + userId);
        }

        //获取普通用户权限级别的所有系统的通知
        Object allUserSuperNotificationCountObj = redisTemplate.opsForValue().get(RedisConstants.ALL_SUPER_NOTIFICATION_COUNT_KEY);
        //获取普通管理员权限级别的所有系统的通知
        Object allAdminSuperNotificationCountObj = redisTemplate.opsForValue().get(RedisConstants.ALL_SUPER_NOTIFICATION_ADMIN_COUNT_KEY);

        Map<String,Integer> countMap = new HashMap<>();


        //判断是否通知缓存是否为空，如果为空缓存到redis
        if (notificationObj == null && superNotificationObj == null){

            //查询普通通知未读数量
            Integer notificationCount = notificationMapper.getNotification(userId);

            //查询系统通知已读数量
            Integer superNotificationCount = superNotificationMapper.getSystemNotification2(userId);

            /**
             * 各权限等级所有通知
             */
            //查询所有普通用户权限系统通知数量
            Integer allUserSuperNotificationCount = superNotificationMapper.getAllUserSuperNotificationCount();
            //查询所有管理员权限系统通知数量
            Integer allAdminSuperNotificationCount = superNotificationMapper.getAllAdminSuperNotificationCount();

            //判断用户权限
            if (userLevel == 2 || userLevel == 1) {
                //管理员通知数量缓存到redis
                redisTemplate.execute(adminNotificationScript,Collections.emptyList(),userId,notificationCount,allAdminSuperNotificationCount,superNotificationCount);
                countMap.put("NotificationCount",notificationCount);
                countMap.put("superNotificationCount",allAdminSuperNotificationCount - superNotificationCount);
                countMap.put("allNotificationCount",notificationCount + (allAdminSuperNotificationCount - superNotificationCount));
            } else {
                //所有用户通知数量缓存到redis
                redisTemplate.execute(userNotificationScript,Collections.emptyList(),userId,notificationCount, allUserSuperNotificationCount,superNotificationCount);
                countMap.put("NotificationCount",notificationCount);
                countMap.put("superNotificationCount",allUserSuperNotificationCount - superNotificationCount);
                countMap.put("allNotificationCount",notificationCount + (allUserSuperNotificationCount - superNotificationCount));
            }

            return countMap;
        }


        /**
         *  Obj转Integer
         */
        Integer notification =
                notificationObj == null
                        ? 0
                        : Integer.valueOf(notificationObj.toString());

        Integer superNotification =
                superNotificationObj == null
                        ? 0
                        : Integer.valueOf(superNotificationObj.toString());

        Integer allUserSuperNotificationCount =
                allUserSuperNotificationCountObj == null
                        ? 0
                        : Integer.valueOf(allUserSuperNotificationCountObj.toString());

        Integer allAdminSuperNotificationCount =
                allAdminSuperNotificationCountObj == null
                        ? 0
                        : Integer.valueOf(allAdminSuperNotificationCountObj.toString());

        //判断用户权限
        if (userLevel == 2 || userLevel == 1) {
            countMap.put("NotificationCount",notification);
            countMap.put("superNotificationCount",allAdminSuperNotificationCount - superNotification);
            countMap.put("allNotificationCount",notification + (allAdminSuperNotificationCount - superNotification));
        } else {
            countMap.put("NotificationCount",notification);
            countMap.put("superNotificationCount",allUserSuperNotificationCount - superNotification);
            countMap.put("allNotificationCount",notification + (allUserSuperNotificationCount - superNotification));
        }


        //不为空就直接返回集合
        return countMap;

    }
}
