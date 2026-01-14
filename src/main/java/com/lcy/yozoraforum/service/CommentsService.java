package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.dto.ShowCommentDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Comments;
import com.lcy.yozoraforum.vo.CommentsVO;

import java.util.List;

public interface CommentsService {

    /**
     * 用户发表评论
     * @param insertCommentsDTO
     */
    Comments insertComment(InsertCommentsDTO insertCommentsDTO);

    /**
     * 显示评论列表
     * @param showForumDTO
     * @return
     */
    List<CommentsVO> showComment(ShowForumDTO showForumDTO);

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
}
