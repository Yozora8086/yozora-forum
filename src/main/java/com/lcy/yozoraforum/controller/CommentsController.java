package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.CommentPositionDTO;
import com.lcy.yozoraforum.dto.DeleteCommentDTO;
import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.dto.ShowCommentDTO;
import com.lcy.yozoraforum.entity.Comments;
import com.lcy.yozoraforum.service.CommentsService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.CommentCountVO;
import com.lcy.yozoraforum.vo.CommentsVO;
import com.lcy.yozoraforum.vo.InsertCommentVO;
import com.lcy.yozoraforum.vo.PositionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 分页查询该帖子的顶级评论
     * @param forumId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/showCommentsList")
    public Result<List<CommentsVO>> showCommentsList(Long forumId, int page, int pageSize){
        List<CommentsVO> commentsVOList = commentsService.showComment(forumId,page,pageSize);
        return Result.success(commentsVOList);
    }

    /**
     * 统计当前帖子下全部的评论
     * @param forumId
     * @return
     */
    @GetMapping("/getAllComment")
    public Result<CommentCountVO> selectAllComment(@RequestParam Long forumId){
        CommentCountVO commentCountVO = commentsService.selectAllComments(forumId);
        return Result.success(commentCountVO);
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
