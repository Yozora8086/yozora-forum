package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Tags;
import com.lcy.yozoraforum.wrapper.ForumWrapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ForumTagRelationMapper {
    /**
     * 将论坛帖子id和分类标签id进行绑定到帖子分类标签表(将论坛帖子id和分类标签id插入帖子分类标签关联表)
     * @param tagsList
     * @param forumId
     */
    void insert(List<Long> tagsList,Long forumId);

    /**
     * 根据帖子id查询当前帖子所添加的分类标签
     * @param showForumDTO
     * @return
     */
    List<Tags> selectTags(ShowForumDTO showForumDTO);

    /**
     * 根据帖子id查询当前帖子所添加的分类标签
     * @param forumId
     * @return
     */
    List<Tags> selectTags(Long forumId);

    /**
     * 删除帖子分类标签关联表 (子表)
     * @param forumId
     */
    @Delete("delete from forum_tag_relation where forum_id = #{forumId}")
    void delete(Long forumId);


    /**
     * 根据分类标签查询帖子
     * @param tagIds
     * @return
     */
    List<ForumWrapper> selectForumByTags(List<Long> tagIds);
}
