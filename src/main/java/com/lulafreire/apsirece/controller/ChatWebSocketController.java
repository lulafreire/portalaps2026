package com.lulafreire.apsirece.controller;

import com.lulafreire.apsirece.model.Mensagem;
import com.lulafreire.apsirece.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    /**
     * Rota para mensagens privadas (Chat Individual)
     * O remetente envia para /app/chat.privado
     */
    @SuppressWarnings("null")
    @MessageMapping("/chat.privado")
    public void processarMensagemPrivada(@Payload Mensagem mensagem) {
        // 1. Salva no MySQL (porta 3307)
        Mensagem mensagemSalva = chatService.salvarMensagem(mensagem);

        // 2. Envia para o destinatário específico
        // O frontend deve se inscrever em /user/{email}/queue/messages
        messagingTemplate.convertAndSendToUser(
                mensagem.getDestinatario().getEmail(),
                "/queue/messages",
                mensagemSalva);
    }

    /**
     * Rota para mensagens de grupo
     * O remetente envia para /app/chat.grupo
     */
    @SuppressWarnings("null")
    @MessageMapping("/chat.grupo")
    public void processarMensagemGrupo(@Payload Mensagem mensagem) {
        // 1. Salva a mensagem vinculada ao grupo_id
        Mensagem mensagemSalva = chatService.salvarMensagem(mensagem);

        // 2. Envia para todos os inscritos no tópico do grupo
        // O frontend deve se inscrever em /topic/grupo/{id}
        messagingTemplate.convertAndSend(
                "/topic/grupo/" + mensagem.getGrupo().getId(),
                mensagemSalva);
    }
}