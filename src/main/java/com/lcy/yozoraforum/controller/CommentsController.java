package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.CommentPositionDTO;
import com.lcy.yozoraforum.dto.DeleteCommentDTO;
import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.dto.ShowCommentDTO;
import com.lcy.yozoraforum.entity.Comments;
import com.lcy.yozoraforum.service.CommentsService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.InsertCommentVO;
import com.lcy.yozoraforum.vo.PositionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 用户删除评论
     * @param commentId
     * @return
     */
    @DeleteMapping("/deleteComment/{commentId}")
    public Result deleteComments(@PathVariable Long commentId){
        commentsService.deleteComment(commentId);
        return Result.success("删除成功");
    }

    /**
     * 评论点赞/取消点赞
     */
    @PostMapping("/like")
    public Result like(@RequestBody ShowCommentDTO showCommentDTO){
        boolean flag = commentsService.like(showCommentDTO);

        if (flag == true){
            return Result.success("成功点赞");
        } else {
            return Result.success("取消点赞");
        }
    }

    /**
     * 评论通知跳转前的定位
     * @param commentPositionDTO
     * @return
     */
    @PostMapping("/positionComment")
    public Result<PositionVO> positionComment(@RequestBody CommentPositionDTO commentPositionDTO){

        PositionVO positionVO = commentsService.selectPositon(commentPositionDTO);

        return Result.success(positionVO);

    }
}
