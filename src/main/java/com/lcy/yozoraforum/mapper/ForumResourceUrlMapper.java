package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.entity.ForumResourceUrl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface ForumResourceUrlMapper {

    /**
     * 将帖子url插入数据库
     * @param forumId
     * @param urls
     */
    void inserts(Long forumId,List<String> urls);

    /**
     * 浏览查询帖子所带的资源
     * @param forumId
     * @return
     */
    List<String> select(Long forumId);


    /**
     * 删除帖子所带的资源
     * @param forumId
     */
    void delete(Long forumId);
}
