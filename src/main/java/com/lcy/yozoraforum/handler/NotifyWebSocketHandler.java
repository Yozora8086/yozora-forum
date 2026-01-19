package com.lcy.yozoraforum.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotifyWebSocketHandler extends TextWebSocketHandler {

    private static final Map<Long, WebSocketSession> ONLINE = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
       Long userId = (Long) session.getAttributes().get("userId");
       ONLINE.put(userId,session);
        System.out.println("WebSocket 连接成功，sessionId=" + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("收到:" + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null){
            ONLINE.remove(userId);
        }
    }

    public static void push(Long userId,String msg){
        WebSocketSession s = ONLINE.get(userId);
        if (s != null && s.isOpen()){
            try {
                s.sendMessage(new TextMessage(msg));
            } catch (IOException e){
                System.out.println("推送消息失败 userId=" + userId);
                ONLINE.remove(userId);//清理失效session
            }

        }
    }


}
