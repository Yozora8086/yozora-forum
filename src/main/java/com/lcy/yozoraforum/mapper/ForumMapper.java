package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ForumMapper {
    /**
     * 插入用户发布的论坛帖子
     * @param forum
     */
    @Insert("insert into forum (forum_title,forum_body,forum_like,create_date,update_date,user_id)" +
            "values " +
            "(#{forumTitle},#{forumBody},#{forumLike},#{createDate},#{updateDate},#{userId})")
    @Options(useGeneratedKeys = true,keyProperty = "forumId",keyColumn = "forum_id")
    void insert(Forum forum);

    /**
     * 分页查询帖子
     * @param
     * @param pageSize
     * @return
     */
    List<Forum> showForumList(int offset, int pageSize);


    /**
     * 浏览帖子(进入所选择的帖子)
     * @param showForumDTO
     * @return
     */
    Forum selectForum(ShowForumDTO showForumDTO);

    /**
     * 点赞增量
     * @param forumId
     * @param count
     */
    @Update("update forum set forum_like = #{count} where forum_id = #{forumId}")
    void updateLike(Integer forumId, Integer count);
}
