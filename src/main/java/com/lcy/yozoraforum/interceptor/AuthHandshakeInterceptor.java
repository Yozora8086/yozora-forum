package com.lcy.yozoraforum.interceptor;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.util.JWTUtils;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    /**
     * 在WebSocket完成握手前拦截并传递用户id
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        try {
            HttpHeaders headers = request.getHeaders();
            List<String> cookies = headers.get("Cookie");

            String token = extractToken(cookies);

            if (token == null){
                System.out.println("拒绝握手:没有token");
                return false;
            }

            Claims claims = JWTUtils.checkToken(token);
            Object uid = claims.get("userId");

            if (uid == null) {
                System.out.println("WS handshake failed: no userId in token");
                return false;
            }

            Long userId = ((Number) uid).longValue();
            System.out.printf("握手时的token", userId);

            //将userId绑定到Session
            attributes.put("userId",userId);
            return true;
        } catch (Exception e){
            System.out.println("WS handshake error: " + e.getMessage());
            return false;
        }


    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }

    private String extractToken(List<String> cookies) {
        if (cookies == null || cookies.isEmpty()) {
            return null;
        }

        for (String cookie : cookies) {
            String[] parts = cookie.split(";");
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith("token=")) {
                    return part.substring("token=".length());
                }
            }
        }
        return null;
    }

}
