package com.lulafreire.apsirece.service;

import com.lulafreire.apsirece.model.Mensagem;
import com.lulafreire.apsirece.model.Usuario;
import com.lulafreire.apsirece.repository.MensagemRepository;
import com.lulafreire.apsirece.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class ChatService {

    private final MensagemRepository mensagemRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Salva a mensagem no MySQL (Porta 3307)
     */
    @Transactional
    public Mensagem salvarMensagem(Mensagem mensagem) {
        if (mensagem.getConteudo() == null) {
            throw new IllegalArgumentException("O conteúdo da mensagem não pode ser nulo");
        }
        mensagem.setDataEnvio(LocalDateTime.now());
        mensagem.setLida(false);
        return mensagemRepository.save(mensagem);
    }

    /**
     * Marca todas as mensagens de uma conversa como lidas ao abrir a janela
     */
    @Transactional
    public void marcarComoLidas(String emailDestinatario, String emailRemetente) {
        List<Mensagem> mensagens = mensagemRepository.findChatHistorico(emailRemetente, emailDestinatario);
        mensagens.stream()
                .filter(m -> m.getDestinatario() != null && m.getDestinatario().getEmail().equals(emailDestinatario))
                .forEach(m -> m.setLida(true));
        mensagemRepository.saveAll(mensagens);
    }

    /**
     * Atualiza o status de presença para o indicador verde/cinza
     * Corrigido para aceitar Integer no banco de dados (Porta 3307)
     */
    @Transactional
    public void atualizarStatusOnline(String username, boolean status) {
        // Busca pelo username conforme o diagrama da tb_usuarios
        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            // Converte boolean para Integer (1 = online, 0 = offline)
            usuario.setOnline(status ? 1 : 0);
            usuarioRepository.save(usuario);
        });
    }

    /**
     * Busca o total de mensagens não lidas para o badge da Sidebar
     */
    public Long obterTotalNaoLidas(String email) {
        return mensagemRepository.countByDestinatarioEmailAndLidaFalse(email);
    }
}