package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.mapper.ForumLikeUserRelationMapper;
import com.lcy.yozoraforum.mapper.ForumMapper;
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
import java.util.HashSet;
import java.util.List;
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
        ScanOptions options = ScanOptions.scanOptions()
                .match("like:forum:*")
                .count(100)
                .build();

        //发起scan,拿到游标
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

            //获取帖子点赞用户的ID
            Set<Object> userIdObList = redisTemplate.opsForSet().members(key);
            Set<Integer> userIdList = new HashSet<>();

            //将id遍历放入UserIdList中
            for (Object o : userIdObList) {
                userIdList.add(Integer.valueOf(o.toString()));
            }

            //判断数据库是否存在这条数据，如果存在就代表已经写入过一次了，将其redis中的数据删除
//            int exist = forumLikeUserRelationMapper.isExist(forumId, userIdList);
//
//            if (exist != 0){
//                redisTemplate.delete(key);
//                System.out.println("删除前 key: '" + key + "'");
//                continue;
//            }

            try {
                //写入数据库
                forumLikeUserRelationMapper.insertRelation(forumId,userIdList);
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
            //将Redis中的赞归0
            Object countOb = redisTemplate.opsForValue().getAndSet(key, 0);

            Integer count = Integer.valueOf(countOb.toString());

            //判断数据是否为0
            if (count == null || count <= 0) {
                continue;
            }

            //写入数据库
            forumMapper.updateLike(forumId, count);

        }
        //关闭游标
        cursor.close();

    }
}
