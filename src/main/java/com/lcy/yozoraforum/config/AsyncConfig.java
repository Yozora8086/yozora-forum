package com.lcy.yozoraforum.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // 核心线程数
        executor.setMaxPoolSize(10);    // 最大线程数
        executor.setQueueCapacity(100); // 队列容量
        executor.setThreadNamePrefix("LikeSync-");
        executor.initialize();
        return executor;
    }
}
