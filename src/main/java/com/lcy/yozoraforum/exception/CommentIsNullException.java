package com.lcy.yozoraforum.exception;

/**
 * 评论为空异常
 */
public class CommentIsNullException extends RuntimeException{
    public CommentIsNullException(){
        super("评论为空");
    }
}
