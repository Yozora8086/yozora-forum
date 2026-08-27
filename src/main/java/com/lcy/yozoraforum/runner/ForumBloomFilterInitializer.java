package com.lcy.yozoraforum.runner;

import com.google.common.hash.BloomFilter;
import com.lcy.yozoraforum.mapper.ForumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ForumBloomFilterInitializer implements ApplicationRunner {

    private final BloomFilter<Long> bloomFilter;
    private final ForumMapper forumMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Long> ids = forumMapper.selectAllForumId();
        for (Long id : ids) {
            bloomFilter.put(id);
        }

        System.out.println("帖子 BloomFilter 初始化完成");

    }
}
