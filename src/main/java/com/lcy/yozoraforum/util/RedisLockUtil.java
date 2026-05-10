package com.lcy.yozoraforum.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLockUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取互斥锁
     * @param key
     * @return
     */
    public boolean tryLock(String key){
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return result;
    }

    /**
     * 删除互斥锁
     * @param key
     */
    public void unLock(String key){
        redisTemplate.delete(key);
    }
}
