package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.exception.CommentIsNullException;
import com.lcy.yozoraforum.mapper.CommentsMapper;
import com.lcy.yozoraforum.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcy.yozoraforum.entity.Comments;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;

    /**
     * 用户发表评论
     * @param insertCommentsDTO
     */
    @Override
    public Comments insertComment(InsertCommentsDTO insertCommentsDTO) {
        //校验评论是否为空
        if (insertCommentsDTO.getContent() == null || insertCommentsDTO.getContent().trim().isEmpty()) {
            throw new CommentIsNullException("评论为空");
        }
        commentsMapper.insert(insertCommentsDTO, BaseContext.getCurrentId());
        Comments comments = Comments.builder()
                .forumId(insertCommentsDTO.getForumId())
                .userId(BaseContext.getCurrentId())
                .content(insertCommentsDTO.getContent())
                .parentId(insertCommentsDTO.getParentId())
                .createTime(insertCommentsDTO.getCreateTime())
                .resource(insertCommentsDTO.getResourceId())
                .build();
        return comments;
    }


}
