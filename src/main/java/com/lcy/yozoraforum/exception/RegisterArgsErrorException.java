package com.lcy.yozoraforum.exception;

/**
 * 注册参数
 */
public class RegisterArgsErrorException extends RuntimeException{
    public RegisterArgsErrorException(String msg){
        super(msg);
    }
}
