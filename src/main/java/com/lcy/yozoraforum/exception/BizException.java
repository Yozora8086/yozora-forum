package com.lcy.yozoraforum.exception;

/**
 * 修改/删除 帖子不存在或者越权
 */
public class BizException extends RuntimeException{
    public BizException(String msg){
        super(msg);
    }
}
