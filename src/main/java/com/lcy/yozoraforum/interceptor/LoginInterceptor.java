package com.lcy.yozoraforum.interceptor;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.exception.LoginErrorException;
import com.lcy.yozoraforum.util.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果 handler 不是一个 HandlerMethod（即不是一个 Controller 方法），就返回 true（或者不拦截）。
       if (!(handler instanceof HandlerMethod)){
           return true;
       }


       //从请求头获取令牌token

        String token = request.getHeader("Authorization");

        if (token.isEmpty()){
            throw new LoginErrorException("您尚未登录");
        }

        //校验令牌

        Claims claims = JWTUtils.checkToken(token);
        java.lang.Number userId =(Number) claims.get("userId");
        BaseContext.setCurrentId(userId.longValue());
        return true;
    }


    /**
     * 请求结束后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清理线程中的用户id
        BaseContext.removeCurrentId();
    }
}
