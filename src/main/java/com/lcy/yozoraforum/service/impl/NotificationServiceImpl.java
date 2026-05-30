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
    private DefaultRedisScript superNotificationScript;


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
        //获取当前用户权限等级
        Integer userLevel = BaseContext.getCurrentLevel();

        if (userLevel == 3) {
            //redis执行lua脚本通知：系统通知自减
            redisTemplate.execute(superNotificationScript, Collections.emptyList(),userId,0,1,userLevel);
        }

        if (userLevel == 2 || userLevel == 1) {
            //redis执行lua脚本通知：管理员通知自减
            redisTemplate.execute(superNotificationScript, Collections.emptyList(),userId,0,1,userLevel);
        }
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
            redisTemplate.execute(superNotificationScript, Collections.emptyList(),userId,0,0,userLevel);
        }

        if (userLevel == 2 || userLevel == 1) {
            //redis执行lua脚本通知：管理员系统通知自减
            redisTemplate.execute(superNotificationScript, Collections.emptyList(),userId,0,0,userLevel);
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
        //获取缓存的通知数量集合
        Map<String,Object> map = redisTemplate.opsForHash().entries(RedisConstants.NOTIFICATION_ALL_COUNT_KEY + userId);
        Map<String,Integer> countResultMap = new HashMap<>();
        //类型转换
        map.forEach((k,v) -> {
            countResultMap.put(
                    String.valueOf(k),
                    Integer.valueOf(v.toString())
            );
        });

        //判断是否通知缓存是否为空，如果为空缓存到redis
        if (countResultMap.isEmpty()){
            //查询普通通知未读数量
            Integer notificationCount = notificationMapper.getNotification(userId);

            //查询系统通知未读数量
            Integer superNotification = superNotificationMapper.getSystemNotification(userId);

            //创建map集合
            Map<String,Integer> countMap = new HashMap<>();
            //将各项未读通知加入map集合
            countMap.put("notificationCount",notificationCount);
            countMap.put("superNotificationCount",superNotification);
            countMap.put("allNotificationCount",notificationCount + superNotification);

            //获取当前用户权限等级
            Integer userLevel = BaseContext.getCurrentLevel();

            if (userLevel == 3){
                //普通用户通知数量缓存到redis
                redisTemplate.opsForHash().putAll(RedisConstants.NOTIFICATION_ALL_COUNT_KEY + userId,countMap);
            }

            if (userLevel == 2 || userLevel == 1) {
                //管理员通知数量缓存到redis
                redisTemplate.opsForHash().putAll(RedisConstants.ADMIN_NOTIFICATION_ALL_COUNT_KEY + userId,countMap);
            }

            return countMap;
        }

        //不为空就直接返回集合
        return countResultMap;

    }
}
