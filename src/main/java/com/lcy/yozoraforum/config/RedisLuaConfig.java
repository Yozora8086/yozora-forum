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

    @Bean
    public DefaultRedisScript superNotificationScript(){
        DefaultRedisScript script = new DefaultRedisScript();
        script.setLocation(new ClassPathResource("lua/superNotification.lua"));
        return script;
    }
}
