package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.wrapper.ForumLikeUserRelationWrapper;
import org.apache.ibatis.annotations.Delete;
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

    int isExist(@Param("forumId") Long forumId, @Param("userIdList") Set<Long> userIdList);

    /**
     * 删除我发布的帖子 删除子表
     * @param forumId
     * @param
     */
    @Delete("delete from forum_like_user_relation where forum_id = #{forumId} and user_id = #{userId}")
    void delete(Long forumId, Long userId);
}
