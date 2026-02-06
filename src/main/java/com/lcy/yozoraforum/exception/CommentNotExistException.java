package com.lcy.yozoraforum.exception;


public class CommentNotExistException extends RuntimeException{

    /**
     * 评论不存在异常
     * @param msg
     */
    public  CommentNotExistException(String msg){
        super(msg);
    }
}
