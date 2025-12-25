package com.lcy.yozoraforum.service;

import com.alibaba.fastjson.JSONObject;
import com.lcy.yozoraforum.mapper.ForumLikeUserRelationMapper;
import com.lcy.yozoraforum.mapper.ForumMapper;
import com.lcy.yozoraforum.wrapper.ForumLikeUserRelationWrapper;
import io.lettuce.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LikeSyncTask {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private ForumMapper forumMapper;
    @Autowired
    private ForumLikeUserRelationMapper forumLikeUserRelationMapper;


    @Scheduled(fixedDelay = 10000)
    @Async("taskExecutor")
    public void syncLikeUser(){
        //扫描帖子点赞的用户数据
        ScanOptions options = ScanOptions.scanOptions()
                .match("like:forum:*")
                .count(100)
                .build();


        //发起scan,拿到游标(扫描帖子点赞的用户数据)
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
                .getConnection()
                .scan(options);


        while (cursor.hasNext()){
            //将字节转换成字符串(Redis协议底层就是字节,)
            String key = new String(cursor.next(), StandardCharsets.UTF_8);
            //判断数据的键是否为空
            if (key.endsWith(":")){
                continue;
            }
            //删除like:forum:前缀
            String forumIdStr = key.substring("like:forum:".length());

            Integer forumId;
            try {
                //类型转换
                forumId = Integer.valueOf(forumIdStr);
            } catch (NumberFormatException e){
                continue;
            }

            //获取该帖子点赞用户的集合
            Map<Object,Object> userIdMap = redisTemplate.opsForHash().entries(key);
            //用于执行mybatis语句的参数集合
            Set<ForumLikeUserRelationWrapper> userIdList = new HashSet<>();

            //遍历该帖子点赞用户的集合
            for (Map.Entry<Object, Object> entry : userIdMap.entrySet()) {
                //获取键
                Integer userId = Integer.valueOf(entry.getKey().toString());
                //获取值
                String jsonValue = entry.getValue().toString();

                //获取的值是json数据，进行解析
                JSONObject object = JSONObject.parseObject(jsonValue);
                Integer status = object.getInteger("status");
                Long ts = object.getLong("ts");

                //帖子用户点赞关联wrapper对象赋值
                ForumLikeUserRelationWrapper forumLikeUserRelationWrapper = ForumLikeUserRelationWrapper.builder()
                        .forumId(forumId)//被点赞帖子id
                        .userId(userId)//点赞用户id
                        .status(status)//点赞状态(0是取消状态/1是点赞状态)
                        .updateTime(LocalDateTime.ofInstant(
                                //将毫秒转换成(yyyy-MM-dd HH:mm:ss)
                                Instant.ofEpochMilli(ts),
                                //指定时区，系统默认
                                ZoneId.systemDefault()))
                        .build();
                //将对象加入到集合中用于批量写入
                userIdList.add(forumLikeUserRelationWrapper);
            }


            try {
                //写入数据库
                forumLikeUserRelationMapper.insertRelation(userIdList);
            } catch (DuplicateKeyException e) {

            }

        }
        //关闭游标
        cursor.close();

    }

    @Scheduled(fixedDelay = 10000)
    @Async("taskExecutor")
    public void syncLikeCount(){
        //构造scan参数
        ScanOptions options = ScanOptions.scanOptions()
                .match("like:count:*")  //要拿的数据
                .count(100)  //每次扫描100个
                .build();

        //发起scan,拿到游标
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
                .getConnection()
                .scan(options);

        //循环遍历游标
        while (cursor.hasNext()){
            //将字节转换成字符串(Redis协议底层就是字节,)
            String key = new String(cursor.next(), StandardCharsets.UTF_8);
            //判断数据的键是否为空
            if (key.endsWith(":")){
                continue;
            }
            //删除like:count:*前缀
            String forumIdStr = key.substring("like:count:".length());
            Integer forumId;
            try {
                //类型转换
                forumId = Integer.valueOf(forumIdStr);
            } catch (NumberFormatException e){
                continue;
            }

            Object countOb = redisTemplate.opsForValue().get(key);

            Integer count = Integer.valueOf(countOb.toString());

            //写入数据库
            forumMapper.updateLike(forumId, count);

        }
        //关闭游标
        cursor.close();

    }
}
