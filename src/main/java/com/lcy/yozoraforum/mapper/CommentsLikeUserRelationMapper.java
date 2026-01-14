package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.wrapper.CommentsLikeUserRelationWrapper;
import com.lcy.yozoraforum.wrapper.ForumLikeUserRelationWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface CommentsLikeUserRelationMapper {
    /**
     * 插入用户评论点赞数据
     * @param
     */
    void insertRelation(Set<CommentsLikeUserRelationWrapper> relationList);
}
