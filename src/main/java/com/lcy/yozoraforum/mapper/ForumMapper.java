package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.dto.UpdateForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import org.apache.ibatis.annotations.*;

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
    List<Forum> showForumList(@Param("offset") int offset,@Param("pageSize") int pageSize);


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
    void updateLike(Long forumId, Integer count);

    /**
     * 分页查询我的帖子
     * @param
     * @param pageSize
     * @return
     */
    List<Forum> showMineForumList(int offset, int pageSize,Long userId);

    /**
     * 修改我发布的帖子
     * @param updateForumDTO
     * @return 返回受影响的行数，1 表示修改成功，0 表示无权限或帖子不存在
     */
    @Update("update forum set forum_title = #{updateForumDTO.forumTitle},forum_body = #{updateForumDTO.forumBody},update_date = now()" +
            " where user_id = #{userId} and forum_id = #{updateForumDTO.forumId}")
    int updateForum(@Param("updateForumDTO") UpdateForumDTO updateForumDTO,@Param("userId") Long userId);

    /**
     * 删除我发布的帖子
     * @param forumId
     * @param userId
     * @return
     */
    @Delete("delete from forum where forum_id = #{forumId} and user_id = #{userId}")
    int deleteForum(Long forumId, Long userId);
}
