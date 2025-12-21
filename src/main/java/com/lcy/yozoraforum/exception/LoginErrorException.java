package com.lcy.yozoraforum.exception;

/**
 * 登录账号密码错误异常
 */
public class LoginErrorException extends RuntimeException{
     public LoginErrorException(String msg){
         super(msg);
     }
}
