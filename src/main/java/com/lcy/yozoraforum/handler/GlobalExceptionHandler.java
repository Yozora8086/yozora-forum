package com.lcy.yozoraforum.handler;

import com.lcy.yozoraforum.exception.CommentIsNullException;
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

}
