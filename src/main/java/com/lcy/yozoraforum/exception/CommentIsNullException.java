package com.lcy.yozoraforum.exception;

/**
 * 评论为空异常
 */
public class CommentIsNullException extends RuntimeException{
    public CommentIsNullException(String msg){
        super(msg);
    }
}
