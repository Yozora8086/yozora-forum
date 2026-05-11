package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.ForumPVDTO;
import com.lcy.yozoraforum.mapper.ForumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PVSync {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ForumMapper forumMapper;

    @Scheduled(fixedDelay = 60000)
    public void syncToMySQL(){
        List<ForumPVDTO> PVList= new ArrayList<>();

        ScanOptions options = ScanOptions.scanOptions()
                .match("forum:cachePV:*")
                .count(100)
                .build();

        Cursor<String> cursor = redisTemplate.scan(options);

        while (cursor.hasNext()){
            String key = cursor.next();
            String PV = (String) redisTemplate.opsForHash().get(key,"PV");
            if (PV == null){
                continue;
            }

            Long forumId = Long.parseLong(key.replace("forum:cachePV:",""));
            PVList.add(new ForumPVDTO(forumId,Long.parseLong(PV)));
        }

        if (!PVList.isEmpty()){
            for (ForumPVDTO forumPVDTO : PVList) {
                forumMapper.PVToMySQL(forumPVDTO);
            }
        }
    }



}
