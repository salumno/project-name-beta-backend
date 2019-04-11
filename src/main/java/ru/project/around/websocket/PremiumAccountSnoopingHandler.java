package ru.project.around.websocket;

import lombok.SneakyThrows;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.project.around.model.PremiumAccountSnoopingNotification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PremiumAccountSnoopingHandler extends TextWebSocketHandler {
    private Map<Long, WebSocketSession> sessions;

    public PremiumAccountSnoopingHandler() {
        sessions = new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        final Long userId = (Long) session.getAttributes().get("userId");
        sessions.put(userId, session);
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @SneakyThrows
    public void sendAccountSnoopingNotification(final PremiumAccountSnoopingNotification notification) {
        sessions.get(notification.getSnoopedUserId()).sendMessage(new TextMessage(notification.getSnooperId().toString()));
    }
}
