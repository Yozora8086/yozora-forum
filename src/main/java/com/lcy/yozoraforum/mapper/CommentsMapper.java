package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.vo.CommentsVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommentsMapper {
    void insert(@Param("insertCommentsDTO") InsertCommentsDTO insertCommentsDTO,@Param("userId") Long userId);

    /**
     * 获取当前帖子下的所有评论
     * @param forumId
     * @return
     */
    List<CommentsVO> getForumAllComments(Long forumId);

    /**
     * 用户删除评论
     * @param commentId
     */
    @Delete("delete from comments where comment_id = #{commentId} and user_id = #{userId}")
    void delete(@Param("commentId") Long commentId,@Param("userId") Long userId);

    /**
     * 修改评论点赞个数
     * @param commentId
     * @param count
     */
    @Update("update comments set comment_like = #{count} where comment_id = #{commentId}")
    void updateLike(Long commentId, Integer count);
}
