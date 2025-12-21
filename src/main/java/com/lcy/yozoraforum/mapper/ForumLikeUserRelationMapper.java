package com.lcy.yozoraforum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface ForumLikeUserRelationMapper {

    /**
     * 插入帖子点赞用户数据
     * @param forumId
     * @param userId
     */
     void insertRelation(Integer forumId, Set<Integer> userId);

    int isExist(@Param("forumId") Integer forumId, @Param("userIdList") Set<Integer> userIdList);
}
