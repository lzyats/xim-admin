package com.platform.modules.ws.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.platform.common.constant.HeadConstant;
import com.platform.modules.ws.service.HookService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class BootWebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    /**
     * socket 建立成功事件
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 存入缓存
        this.addSession(session);
        // 下发消息
        SpringUtil.getBean(HookService.class).connection();
    }

    /**
     * 接收消息事件
     */
    @SneakyThrows
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 回复消息
        session.sendMessage(new TextMessage("pong"));
    }

    /**
     * socket 断开连接时
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        this.removeSession(session);
    }

    /**
     * socket 断开连接时
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        this.removeSession(session);
    }

    /**
     * 新增
     * session
     */
    private void addSession(WebSocketSession session) {
        SESSIONS.put(session.getId(), session);
    }

    /**
     * 移除session
     */
    private void removeSession(WebSocketSession session) {
        SESSIONS.remove(session.getId());
    }

    /**
     * 所有用户发送消息
     */
    public void sendMsg(String content) {
        SESSIONS.values().forEach((session) -> {
            this.doSendMsg(session, content);
        });
    }

    /**
     * 执行发送消息
     */
    private void doSendMsg(WebSocketSession session, String content) {
        if (session == null || !session.isOpen()) {
            return;
        }
        try {
            session.sendMessage(new TextMessage(content));
        } catch (IOException e) {
            this.removeSession(session);
        }
    }

}