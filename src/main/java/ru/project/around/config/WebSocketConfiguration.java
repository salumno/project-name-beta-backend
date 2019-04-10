package ru.project.around.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.project.around.websocket.PremiumAccountSnoopingHandler;

import java.util.Map;

@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(premiumAccountSnoopingHandler(), "/premium/snooping/{userId}").addInterceptors(premiumAccountInterceptor());
    }

    @Bean
    public HandshakeInterceptor premiumAccountInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) {
                final String path = serverHttpRequest.getURI().getPath();
                final Long userId = Long.valueOf(path.substring(path.lastIndexOf('/') + 1));
                map.put("userId", userId);
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
            }
        };
    }

    @Bean
    public PremiumAccountSnoopingHandler premiumAccountSnoopingHandler() {
        return new PremiumAccountSnoopingHandler();
    }
}
