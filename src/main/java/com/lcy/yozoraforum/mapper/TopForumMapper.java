package com.lcy.yozoraforum.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopForumMapper {
    /**
     * 数据库插入置顶帖子
     * @param forumId
     */
    void insertTopForum(Long forumId);

    Integer getAllTopForumCount();
}
