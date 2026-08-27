package com.lcy.yozoraforum.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
布隆过滤器
 */
@Configuration
public class BloomFilterConfiguration {

    @Bean
    public BloomFilter<Long> fourmBloomFilter(){
        return BloomFilter.create(Funnels.longFunnel(),100000,0.01);
    }
}
