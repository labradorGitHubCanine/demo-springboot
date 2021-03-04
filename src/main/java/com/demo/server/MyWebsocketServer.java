package com.demo.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/websocket")
public class MyWebsocketServer {

    private static final Map<String, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("有新的客户端连接了：{}", session.getId());
        clients.put(session.getId(), session);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("有用户断开了，id为：{}", session.getId());
        clients.remove(session.getId());
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("服务端收到客户端 {} 发来的消息: {}", session.getId(), message);
        sendAll(message);
    }

    private void sendAll(String message) {
        for (Map.Entry<String, Session> e : clients.entrySet())
            e.getValue().getAsyncRemote().sendText(message);
    }

}
