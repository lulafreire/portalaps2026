package com.lulafreire.apsirece.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.lulafreire.apsirece.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final UsuarioRepository usuarioRepository;

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        String username = extrairUsername(event.getMessage());
        if (username != null)
            atualizarOnline(username, true);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String username = extrairUsername(event.getMessage());
        if (username != null)
            atualizarOnline(username, false);
    }

    // ✅ Extração do username com verificação explícita de null em cada etapa
    private String extrairUsername(@NonNull org.springframework.messaging.Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        java.security.Principal user = accessor.getUser();
        if (user == null)
            return null;
        String name = user.getName();
        return (name != null && !name.isBlank()) ? name : null;
    }

    private void atualizarOnline(String username, boolean online) {
        usuarioRepository.findByUsername(username).ifPresent(u -> {
            u.setOnline(online ? 1 : 0);
            usuarioRepository.save(u);

            // ✅ HashMap em vez de Map.of() para evitar aviso de tipo não verificado
            Map<String, Object> status = new HashMap<>();
            status.put("username", username);
            status.put("online", online);

            messagingTemplate.convertAndSend("/topic/status", status);
        });
    }
}