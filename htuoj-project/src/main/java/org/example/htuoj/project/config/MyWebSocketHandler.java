//package org.example.htuoj.project.config;
//
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class MyWebSocketHandler extends TextWebSocketHandler {
//
//    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.put(session.getId(), session);
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        // 处理消息，例如广播给所有客户端
//        broadcastMessage(payload);
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
//        sessions.remove(session.getId());
//    }
//
//    private void broadcastMessage(String message) {
//        for (WebSocketSession session : sessions.values()) {
//            try {
//                session.sendMessage(new TextMessage(message));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}