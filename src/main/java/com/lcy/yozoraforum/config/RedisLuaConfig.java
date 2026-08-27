package com.lcy.yozoraforum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisLuaConfig {
    /**
     * 注册缓存帖子信息lua脚本
     * @return
     */
    @Bean
    public DefaultRedisScript<Long> cacheForumAndForumPVScript(){

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/cachePV.lua"));
        script.setResultType(Long.class);

        return script;
    }

    /**
     * 注册更新已缓存帖子信息的TTL
     * @return
     */
    @Bean
    public DefaultRedisScript<String> checkTTLScript(){
        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/checkTTL.lua"));
        script.setResultType(String.class);
        return script;
    }

    /**
     * 管理通知脚本
     * @return
     */
    @Bean
    public DefaultRedisScript adminNotificationScript(){
        DefaultRedisScript script = new DefaultRedisScript();
        script.setLocation(new ClassPathResource("lua/adminNotification.lua"));
        return script;
    }

    /**
     * 普通用户通知脚本
     * @return
     */
    @Bean
    public DefaultRedisScript userNotificationScript(){
        DefaultRedisScript script = new DefaultRedisScript();
        script.setLocation(new ClassPathResource("lua/userNotification.lua"));
        return script;
    }

    /**
     * 记录用户1分钟内请求次数脚本
     * @return
     */
    @Bean
    public DefaultRedisScript<Long> userRequestCountScript(){
        DefaultRedisScript<Long> script = new DefaultRedisScript();
        script.setLocation(new ClassPathResource("lua/userRequestCount.lua"));
        script.setResultType(Long.class);
        return script;
    }
}
