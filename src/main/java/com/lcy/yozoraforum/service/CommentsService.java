package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.CommentPositionDTO;
import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.dto.ShowCommentDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Comments;
import com.lcy.yozoraforum.vo.CommentCountVO;
import com.lcy.yozoraforum.vo.CommentsVO;
import com.lcy.yozoraforum.vo.PositionVO;

import java.util.List;

public interface CommentsService {

    /**
     * 用户发表评论
     * @param insertCommentsDTO
     */
    Comments insertComment(InsertCommentsDTO insertCommentsDTO);

    /**
     * 分页查询该帖子的顶级评论
     * @param forumId
     * @return
     */
    List<CommentsVO> showComment(Long forumId,int page,int pageSize);

    /**
     * 用户删除评论
     * @param commentId
     */
    void deleteComment(Long commentId);

    /**
     * 评论点赞/取消点赞
     * @param showCommentDTO
     * @return
     */
    boolean like(ShowCommentDTO showCommentDTO);

    /**
     * 评论通知跳转前的定位
     * @param commentPositionDTO
     * @return
     */
    PositionVO selectPositon(CommentPositionDTO commentPositionDTO);

    /**
     * 统计当前帖子下全部的评论
     * @param forumId
     * @return
     */
    CommentCountVO selectAllComments(Long forumId);
}
