package com.lulafreire.apsirece.controller;

import com.lulafreire.apsirece.service.LinkService;
import com.lulafreire.apsirece.service.ServidorService;
import com.lulafreire.apsirece.service.DocumentoService;
import com.lulafreire.apsirece.model.CategoriaLink;
import com.lulafreire.apsirece.model.Grupo;
import com.lulafreire.apsirece.model.Usuario;
import com.lulafreire.apsirece.repository.GrupoRepository;
import com.lulafreire.apsirece.repository.ServidorRepository;
import com.lulafreire.apsirece.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final LinkService linkService;
    private final ServidorService servidorService;
    private final DocumentoService documentoService;
    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServidorRepository servidorRepository;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        // 1. Verificação de Autenticação com proteção contra Null
        boolean estaLogado = (authentication != null && authentication.isAuthenticated());
        model.addAttribute("estaLogado", estaLogado);

        List<Grupo> grupos = new ArrayList<>();

        // Bloco seguro: authentication é verificado explicitamente para evitar avisos
        // do compilador
        if (estaLogado && authentication != null) {
            String usernameLogado = authentication.getName();

            Usuario usuario = usuarioRepository.findByUsername(usernameLogado).orElse(null);
            model.addAttribute("usuario", usuario);

            // Lógica de Permissões (Admin vs Usuário Comum)
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                grupos = grupoRepository.findAllWithMembrosOrdered();
            } else {
                // Busca em dois passos para garantir a contagem correta de membros
                List<Long> ids = grupoRepository.findIdsByMembroUsername(usernameLogado);
                if (!ids.isEmpty()) {
                    grupos = grupoRepository.findAllByIdInWithMembros(ids);
                }
            }

            // Suprime o usuário logado da lista lateral de Servidores
            model.addAttribute("servidores", servidorRepository.findAll().stream()
                    .filter(s -> s.getUsuario() != null && !s.getUsuario().getUsername().equals(usernameLogado))
                    .toList());
        } else {
            // Caso não esteja logado, exibe todos os servidores normalmente
            model.addAttribute("servidores", servidorRepository.findAll());
        }

        model.addAttribute("grupos", grupos);

        // 2. Carregamento de dados para os fragmentos da Home
        model.addAttribute("linksSistemas", linkService.listarPorCategoria(CategoriaLink.SISTEMA));
        model.addAttribute("linksFormularios", linkService.listarPorCategoria(CategoriaLink.FORMULARIO));
        model.addAttribute("linksExternos", linkService.listarPorCategoria(CategoriaLink.LINK_EXTERNO));
        model.addAttribute("linksManuais", linkService.listarPorCategoria(CategoriaLink.MANUAL));

        model.addAttribute("totalNaoLidas", 0);
        model.addAttribute("aniversariantes", servidorService.buscarAniversariantes());
        model.addAttribute("maisVisitados", linkService.listarMaisVisitados());
        model.addAttribute("totalDocumentos", documentoService.contarDocumentos());

        return "index";
    }

    @GetMapping("/equipe")
    public String paginaEquipe(Model model) {
        model.addAttribute("aniversariantes", servidorService.buscarAniversariantes());
        model.addAttribute("maisVisitados", linkService.listarMaisVisitados());
        return "equipe";
    }

    @GetMapping("/digital")
    public String digitalPage(@RequestParam(value = "dataFiltro", required = false) LocalDate dataFiltro, Model model) {
        LocalDate dataBusca = (dataFiltro == null) ? LocalDate.now() : dataFiltro;
        model.addAttribute("arquivos", documentoService.buscarPorData(dataBusca));
        model.addAttribute("dataAtual", dataBusca);
        return "digital";
    }

    @GetMapping("/utilitarios")
    public String utilitariosPage(Model model) {
        return "utilitarios";
    }

    @GetMapping("/arquivos")
    public String arquivosPage(Model model) {
        model.addAttribute("listaArquivos", documentoService.listarTodos());
        return "arquivos";
    }

    @GetMapping("/tarefas")
    public String tarefasPage(Model model) {
        return "tarefas";
    }

    @GetMapping("/opcoes")
    public String opcoesPage(Model model) {
        return "opcoes";
    }
}