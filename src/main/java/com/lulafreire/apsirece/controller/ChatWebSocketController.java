package com.lulafreire.apsirece.controller;

import com.lulafreire.apsirece.model.Mensagem;
import com.lulafreire.apsirece.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

//@Controller
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @SuppressWarnings("null")
    @MessageMapping("/chat.privado")
    public void processarMensagemPrivada(@Payload Mensagem mensagem) {
        Mensagem mensagemSalva = chatService.salvarMensagem(mensagem);

        // Envia para o tópico privado do destinatário
        messagingTemplate.convertAndSendToUser(
                mensagem.getDestinatario().getEmail(),
                "/queue/messages",
                mensagemSalva);
    }

    // Adicionado .{id} para casar com o envio do frontend
    @SuppressWarnings("null")
    @MessageMapping("/chat.grupo.{id}")
    public void processarMensagemGrupo(@DestinationVariable Long id, @Payload Mensagem mensagem) {
        // Garante que o ID do grupo na mensagem seja o da rota
        if (mensagem.getGrupo() != null) {
            mensagem.getGrupo().setId(id);
        }

        Mensagem mensagemSalva = chatService.salvarMensagem(mensagem);

        // Envia para o tópico que usa PONTO (conforme seu JS subscribe)
        messagingTemplate.convertAndSend("/topic/grupo." + id, mensagemSalva);
    }
}