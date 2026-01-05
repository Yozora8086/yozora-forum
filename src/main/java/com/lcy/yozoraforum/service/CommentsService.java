package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.InsertCommentsDTO;
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
}
