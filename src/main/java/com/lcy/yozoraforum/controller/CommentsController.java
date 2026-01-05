package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.entity.Comments;
import com.lcy.yozoraforum.service.CommentsService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.InsertCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yozora/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    /**
     * 用户发表评论
     * @param insertCommentsDTO
     * @return
     */
    @PostMapping("/insert")
    public Result<InsertCommentVO> insertComments(@RequestBody InsertCommentsDTO insertCommentsDTO){

        Comments comment = commentsService.insertComment(insertCommentsDTO);
        InsertCommentVO insertCommentVO = InsertCommentVO.builder()
                .forumId(comment.getForumId())
                .userId(BaseContext.getCurrentId())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .createTime(comment.getCreateTime())
                .resourceId(comment.getResource())
                .build();
        return Result.success(insertCommentVO);

    }
}
