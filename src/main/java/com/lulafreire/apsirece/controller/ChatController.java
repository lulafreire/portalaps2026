package com.lulafreire.apsirece.controller;

import com.lulafreire.apsirece.model.Grupo;
import com.lulafreire.apsirece.model.Usuario;
import com.lulafreire.apsirece.model.Servidor;
import com.lulafreire.apsirece.model.Mensagem;
import com.lulafreire.apsirece.repository.GrupoRepository;
import com.lulafreire.apsirece.repository.MensagemRepository;
import com.lulafreire.apsirece.repository.ServidorRepository;
import com.lulafreire.apsirece.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/mensagens")
    public String abrirMensagensGeral(Model model, Authentication authentication) {
        boolean estaLogado = (authentication != null && authentication.isAuthenticated());
        model.addAttribute("estaLogado", estaLogado);
        prepararAside(model, authentication);
        return "chat/principal";
    }

    @GetMapping("/historico/{tipo}/{id}")
    public String carregarHistorico(@PathVariable String tipo, @PathVariable Long id, Model model,
            Authentication authentication) {

        System.out.println(">>> Iniciando carregarHistorico - Tipo: " + tipo + " | ID: " + id);

        if (id == null || authentication == null) {
            System.err.println(">>> Erro: ID ou Autenticação nulos.");
            return "chat/principal :: area-mensagens";
        }

        try {
            model.addAttribute("estaLogado", true);

            if ("grupo".equalsIgnoreCase(tipo)) {
                System.out.println(">>> Processando histórico de GRUPO");
                Grupo grupo = grupoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));

                model.addAttribute("tipoConversa", "GRUPO");
                model.addAttribute("conversaAtiva", grupo);
                model.addAttribute("mensagens", mensagemRepository.findByGrupoIdOrderByDataEnvioAsc(id));
                System.out.println(">>> Histórico de grupo carregado com sucesso.");

            } else if ("privado".equalsIgnoreCase(tipo)) {
                System.out.println(">>> Processando histórico PRIVADO");

                // 1. Busca remetente (Logado)
                String usernameLogado = authentication.getName();
                System.out.println(">>> Usuário logado: " + usernameLogado);
                Usuario remetente = usuarioRepository.findByUsername(usernameLogado)
                        .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));

                // 2. Busca destinatário (Usuário)
                Usuario destinatario = usuarioRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Destinatário não encontrado"));
                System.out.println(">>> Destinatário encontrado: " + destinatario.getUsername() + " (ID: " + id + ")");

                // 3. Busca vínculo de servidor para exibir o Nome Completo
                Servidor servidorDestino = servidorRepository.findAll().stream()
                        .filter(s -> s.getUsuario() != null && s.getUsuario().getId().equals(id))
                        .findFirst()
                        .orElse(null);

                if (servidorDestino != null) {
                    System.out.println(">>> Vínculo de servidor encontrado: " + servidorDestino.getNomeCompleto());
                } else {
                    System.out.println(">>> Aviso: Nenhum vínculo de servidor encontrado para este usuário.");
                }

                // Adiciona ao modelo garantindo que as chaves batam com o HTML
                model.addAttribute("tipoConversa", "PRIVADO");
                model.addAttribute("destinatario", destinatario); // Usado para IDs/Avatar
                model.addAttribute("servidorDestino", servidorDestino); // Usado para o Nome Exibido

                // 4. Busca mensagens
                System.out.println(">>> Buscando mensagens no repositório entre IDs " + remetente.getId() + " e "
                        + destinatario.getId());
                List<Mensagem> mensagens = mensagemRepository.findChatPrivado(remetente.getId(), destinatario.getId());

                model.addAttribute("mensagens", mensagens != null ? mensagens : new ArrayList<>());
                System.out.println(
                        ">>> Mensagens privadas carregadas. Total: " + (mensagens != null ? mensagens.size() : 0));
            }

        } catch (Exception e) {
            System.err.println(">>> EXCEÇÃO no ChatController: " + e.getMessage());
            e.printStackTrace(); // Imprime a pilha de erro completa no terminal para depuração profunda
            model.addAttribute("mensagens", new ArrayList<>());
            model.addAttribute("erro", e.getMessage());
        }

        System.out.println(">>> Renderizando fragmento area-mensagens");
        return "chat/principal :: area-mensagens";
    }

    private void prepararAside(Model model, Authentication authentication) {
        List<Grupo> grupos = new ArrayList<>();

        if (authentication != null && authentication.isAuthenticated()) {
            String usernameLogado = authentication.getName();
            model.addAttribute("estaLogado", true);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                grupos = grupoRepository.findAllWithMembrosOrdered();
            } else {
                List<Long> ids = grupoRepository.findIdsByMembroUsername(usernameLogado);
                if (!ids.isEmpty()) {
                    grupos = grupoRepository.findAllByIdInWithMembros(ids);
                }
            }

            model.addAttribute("servidores", servidorRepository.findAll().stream()
                    .filter(s -> s.getUsuario() != null && !s.getUsuario().getUsername().equals(usernameLogado))
                    .toList());
        } else {
            model.addAttribute("estaLogado", false);
            model.addAttribute("servidores", servidorRepository.findAll());
        }

        model.addAttribute("grupos", grupos);
    }
}