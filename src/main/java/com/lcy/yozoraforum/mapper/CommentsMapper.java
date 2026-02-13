package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.CommentPositionDTO;
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
     * 分页查询该帖子下的顶级评论
     * @param forumId
     * @return
     */
    List<CommentsVO> getForumAllComments(@Param("forumId") Long forumId,@Param("offset") int offset,@Param("pageSize") int pageSize);

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

    /**
     * 评论通知跳转前的定位
     * @param commentPositionDTO
     * @return
     */
    Long selectPosition(CommentPositionDTO commentPositionDTO);

    /**
     * 统计当前帖子下全部的评论
     * @param forumId
     * @return
     */
    Long selectAll(Long forumId);

    /**
     * 统计当前帖子下全部的评论(仅父评论)
     * @param forumId
     * @return
     */
    Long selectNoChildCommentCount(Long forumId);

    /**
     * 分页查询该帖子下的子评论
     * @param commentsVOList
     * @return
     */
    List<CommentsVO> getChildComments(List<CommentsVO> commentsVOList);
}
