package com.lulafreire.apsirece.service;

import com.lulafreire.apsirece.model.Grupo;
import com.lulafreire.apsirece.model.Mensagem;
import com.lulafreire.apsirece.model.Usuario;
import com.lulafreire.apsirece.repository.MensagemRepository;
import com.lulafreire.apsirece.repository.UsuarioRepository;
import com.lulafreire.apsirece.repository.GrupoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MensagemRepository mensagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;

    /**
     * Salva a mensagem no MySQL tratando as relações de forma gerenciada pelo
     * Hibernate.
     */
    @SuppressWarnings("null")
    @Transactional
    public Mensagem salvarMensagem(Mensagem mensagem) {
        // 1. Recupera o remetente real do banco (evita erro de objeto transiente)
        Usuario remetente = usuarioRepository.findByUsername(mensagem.getRemetente().getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        mensagem.setRemetente(remetente);

        // 2. Se for grupo, recupera o grupo real
        if (mensagem.getGrupo() != null && mensagem.getGrupo().getId() != null) {
            Grupo grupo = grupoRepository.findById(mensagem.getGrupo().getId())
                    .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
            mensagem.setGrupo(grupo);
        }

        mensagem.setDataEnvio(LocalDateTime.now());
        return mensagemRepository.save(mensagem);
    }

    @Transactional
    public void marcarComoLidas(String emailDestinatario, String emailRemetente) {
        List<Mensagem> mensagens = mensagemRepository.findChatHistorico(emailRemetente, emailDestinatario);
        mensagens.stream()
                .filter(m -> m.getDestinatario() != null && m.getDestinatario().getEmail().equals(emailDestinatario))
                .forEach(m -> m.setLida(true));
        mensagemRepository.saveAll(mensagens);
    }

    @Transactional
    public void atualizarStatusOnline(String username, boolean status) {
        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            usuario.setOnline(status ? 1 : 0);
            usuarioRepository.save(usuario);
        });
    }

    public Long obterTotalNaoLidas(String email) {
        return mensagemRepository.countByDestinatarioEmailAndLidaFalse(email);
    }
}