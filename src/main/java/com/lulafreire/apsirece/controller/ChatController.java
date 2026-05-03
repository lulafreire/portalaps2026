package com.lulafreire.apsirece.controller;

import com.lulafreire.apsirece.model.*;
import com.lulafreire.apsirece.repository.*;
import com.lulafreire.apsirece.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final GrupoRepository grupoRepository;
    private final ServidorRepository servidorRepository;
    private final UsuarioRepository usuarioRepository;
    private final MensagemRepository mensagemRepository;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // --- MÉTODOS DE WEBSOCKET ---

    /**
     * Recebe mensagens de grupo.
     * Corrigido com .{grupoId} para bater com o log de erro anterior.
     */
    @SuppressWarnings("null")
    @MessageMapping("/chat.grupo.{grupoId}")
    public void processarMensagemGrupo(@DestinationVariable Long grupoId, @Payload Mensagem mensagem) {
        try {
            // Forçamos o ID que veio na URL para garantir a associação correta
            if (mensagem.getGrupo() == null) {
                mensagem.setGrupo(new Grupo());
            }
            mensagem.getGrupo().setId(grupoId);

            // Chamada ao service que deve conter @Transactional
            Mensagem salva = chatService.salvarMensagem(mensagem);

            // O envio para o tópico deve usar o objeto 'salva' que já vem com ID do banco
            messagingTemplate.convertAndSend("/topic/grupo." + grupoId, salva);

        } catch (Exception e) {
            System.err.println("ERRO AO SALVAR MENSAGEM: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recebe mensagens privadas.
     * Padronizado com .{usuarioId} para consistência.
     */
    @SuppressWarnings("null")
    @MessageMapping("/chat.privado.{usuarioId}")
    public void processarMensagemPrivada(@DestinationVariable Long usuarioId, @Payload Mensagem mensagem) {
        try {
            if (mensagem.getDestinatario() != null) {
                mensagem.getDestinatario().setId(usuarioId);
            }

            Mensagem salva = chatService.salvarMensagem(mensagem);

            // Envia para o canal específico do destinatário
            messagingTemplate.convertAndSendToUser(
                    salva.getDestinatario().getUsername(),
                    "/queue/messages",
                    salva);

            // Também envia para o remetente (para atualizar a própria tela)
            messagingTemplate.convertAndSendToUser(
                    salva.getRemetente().getUsername(),
                    "/queue/messages",
                    salva);
        } catch (Exception e) {
            System.err.println("Erro no WebSocket Privado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- MÉTODOS DE VIEW ---

    @GetMapping("/mensagens")
    public String abrirMensagensGeral(Model model, Authentication authentication) {
        boolean estaLogado = (authentication != null && authentication.isAuthenticated());
        model.addAttribute("estaLogado", estaLogado);
        prepararAside(model, authentication);
        return "chat/principal";
    }

    @GetMapping("/historico/{tipo}/{id}")
    @Transactional(readOnly = true)
    public String carregarHistorico(@PathVariable String tipo, @PathVariable Long id, Model model,
            Authentication authentication) {

        if (id == null || authentication == null)
            return "chat/principal :: area-mensagens";

        try {
            model.addAttribute("estaLogado", true);
            String usernameLogado = authentication.getName();

            if ("grupo".equalsIgnoreCase(tipo)) {
                Grupo grupo = grupoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));

                model.addAttribute("tipoConversa", "GRUPO");
                model.addAttribute("conversaAtiva", grupo);
                // Utilizando o método completo para evitar queries N+1 no Hibernate
                model.addAttribute("mensagens", mensagemRepository.findByGrupoIdCompleto(id));

            } else if ("privado".equalsIgnoreCase(tipo)) {
                Usuario remetente = usuarioRepository.findByUsername(usernameLogado)
                        .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));

                Usuario destinatario = usuarioRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Destinatário não encontrado"));

                model.addAttribute("tipoConversa", "PRIVADO");
                model.addAttribute("destinatario", destinatario);

                List<Mensagem> mensagens = mensagemRepository.findChatPrivado(remetente.getId(), destinatario.getId());
                model.addAttribute("mensagens", mensagens != null ? mensagens : new ArrayList<>());
            }

        } catch (Exception e) {
            model.addAttribute("mensagens", new ArrayList<>());
            model.addAttribute("erro", e.getMessage());
        }

        return "chat/principal :: area-mensagens";
    }

    private void prepararAside(Model model, Authentication authentication) {
        List<Grupo> grupos = new ArrayList<>();
        if (authentication != null && authentication.isAuthenticated()) {
            String usernameLogado = authentication.getName();

            // Lógica de visualização baseada em Roles (Admin vê tudo, Usuário vê os seus)
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                grupos = grupoRepository.findAllWithMembrosOrdered();
            } else {
                List<Long> ids = grupoRepository.findIdsByMembroUsername(usernameLogado);
                if (!ids.isEmpty())
                    grupos = grupoRepository.findAllByIdInWithMembros(ids);
            }

            // Lista servidores (exceto o próprio usuário logado)
            model.addAttribute("servidores", servidorRepository.findAll().stream()
                    .filter(s -> s.getUsuario() != null && !s.getUsuario().getUsername().equals(usernameLogado))
                    .toList());
        }
        model.addAttribute("grupos", grupos);
    }
}