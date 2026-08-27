package com.lcy.yozoraforum.interceptor;

import com.lcy.yozoraforum.constant.RedisConstants;
import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.exception.LoginErrorException;
import com.lcy.yozoraforum.exception.TooManyRequestException;
import com.lcy.yozoraforum.util.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TooManyListenersException;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate redisTemplate;

    private final DefaultRedisScript<Long> userRequestCountScript;

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
        //从令牌中获取userId
        java.lang.Number userId =(Number) claims.get("userId");
        //从令牌中获取userLevel
        java.lang.Number userLevel = (Number) claims.get("userLevel");
        BaseContext.setCurrentId(userId.longValue());
        BaseContext.setCurrentLevel(userLevel.intValue());


        Long resultType = 1L;
        //执行请求记录脚本
        resultType = redisTemplate.execute(userRequestCountScript,
                Collections.emptyList(),
                RedisConstants.USER_REQUEST_COUNT,
                userId.toString(),
                "60"
        );

        //判断1分钟请求是否达到阈值，当lua脚本返回0时，则被限流，返回1时则放行
        if (resultType == 0) {
            //请求频繁异常
           throw new TooManyRequestException();
        }

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
        //清理线程中的用户权限等级
        BaseContext.removeCurrentLevel();
    }
}
