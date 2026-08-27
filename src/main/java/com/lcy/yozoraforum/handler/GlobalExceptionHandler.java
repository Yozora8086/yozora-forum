package com.lcy.yozoraforum.handler;

import com.lcy.yozoraforum.exception.*;
import com.lcy.yozoraforum.util.Result;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(RuntimeException ex){
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获token过期异常
     * @param ex
     * @return
     */

    @ExceptionHandler(ExpiredJwtException.class)
    public Result handleExpiredJwtException(ExpiredJwtException ex){
        return Result.error("身份已过期，请重新登录");
    }

    /**
     * 捕获帖子不存在异常
     * @param ex
     * @return
     */
    @ExceptionHandler(ForumNotFindException.class)
    public Result handleForumNotFindException(ForumNotFindException ex){
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获评论不存在异常
     * @param ex
     * @return
     */
    @ExceptionHandler(CommentNotExistException.class)
    public Result handleCommentNotExistException(CommentNotExistException ex){
        return Result.error(ex.getMessage());
    }

    /**
     * 修改/删除 帖子不存在或者越权
     * @param ex
     * @return
     */
    @ExceptionHandler(BizException.class)
    public Result handleBizException(BizException ex){
        return Result.error(ex.getMessage());
    }

    /**
     * 系统消息发送越权
     * @param ex
     * @return
     */
    @ExceptionHandler(PermissionException.class)
    public Result handlePermissionException(PermissionException ex){
        return Result.error("越权操作");
    }

    /**
     * 置顶帖子数量超过最大限制异常
     * @param ex
     * @return
     */
    @ExceptionHandler(MaxTopForumException.class)
    public Result handleMaxTopForumException(MaxTopForumException ex) {
        return Result.error("置顶帖子数量超过最大限制");
    }

    /**
     * 置顶帖子已存在异常
     * @param ex
     * @return
     */
    @ExceptionHandler(TopForumExistException.class)
    public Result handleToForumExistException(TopForumExistException ex) {
        return Result.error("置顶帖子已存在");
    }

    /**
     * 请求过于频繁异常
     * @param ex
     * @return
     */
    @ExceptionHandler(TooManyRequestException.class)
    public Result handleTooManyRequestException(TooManyRequestException ex){
        return Result.error("请求过于频繁，请1分钟后重试");
    }
}
