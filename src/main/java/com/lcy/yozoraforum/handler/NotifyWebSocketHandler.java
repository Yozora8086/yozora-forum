package com.lcy.yozoraforum.handler;

import com.lcy.yozoraforum.context.BaseContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotifyWebSocketHandler extends TextWebSocketHandler {
    //创建一个用户在线表
    private static final Map<Long, WebSocketSession> ONLINE = new ConcurrentHashMap<>();

    /**
     *  WebSocket连接成功时
     * @param session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
       //从session中拿到userId
       Long userId = (Long) session.getAttributes().get("userId");
       //将用户加入在线表
       ONLINE.put(userId,session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("收到:" + message.getPayload());
    }

    /**
     * WebSocket断开连接后
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //从session中拿到userId
        Long userId = (Long) session.getAttributes().get("userId");
        //如果userId为空，userId从在线表中删除
        if (userId != null){
            ONLINE.remove(userId);
        }
    }

    /**
     * 给指定用户发 WebSocket 消息
     * @param userId
     * @param msg
     */
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


//    public static void push(String content, Integer userLevel, LocalDateTime createTime){
//
//
//        Long userId = BaseContext.getCurrentId();
//        WebSocketSession s = ONLINE.get(userId);
//
//        if (s != null && s.isOpen()){
//            try {
//                s.sendMessage(new TextMessage(msg));
//            } catch (IOException e){
//                System.out.println("推送消息失败 userId=" + userId);
//                ONLINE.remove(userId);//清理失效session
//            }
//
//        }
//    }


}
