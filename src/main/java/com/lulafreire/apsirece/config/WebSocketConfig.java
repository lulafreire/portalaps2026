package com.lulafreire.apsirece.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(@SuppressWarnings("null") MessageBrokerRegistry config) {
        // Habilita um broker simples na memória para enviar mensagens aos clientes
        // /topic: mensagens de grupo (Broadcast)
        // /queue: mensagens privadas (Ponto-a-ponto)
        config.enableSimpleBroker("/topic", "/queue");

        // Prefixo para as mensagens que saem do cliente para o servidor
        // (@MessageMapping)
        config.setApplicationDestinationPrefixes("/app");

        // Prefixo específico para mensagens destinadas a usuários específicos
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(@SuppressWarnings("null") StompEndpointRegistry registry) {
        // O endpoint que o cliente usará para se conectar ao WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Permite conexões de diferentes origens se necessário
                .withSockJS(); // Fallback para navegadores que não suportam WebSocket puro
    }
}