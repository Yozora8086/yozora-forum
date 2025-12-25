package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.wrapper.ForumLikeUserRelationWrapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface ForumLikeUserRelationMapper {

    /**
     * 插入帖子点赞用户数据
     * @param
     * @param
     */
     void insertRelation(Set<ForumLikeUserRelationWrapper> relationList);

    int isExist(@Param("forumId") Integer forumId, @Param("userIdList") Set<Integer> userIdList);
}
