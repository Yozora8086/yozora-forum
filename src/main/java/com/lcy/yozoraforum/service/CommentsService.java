package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.entity.Comments;

public interface CommentsService {

    /**
     * 用户发表评论
     * @param insertCommentsDTO
     */
    Comments insertComment(InsertCommentsDTO insertCommentsDTO);
}
