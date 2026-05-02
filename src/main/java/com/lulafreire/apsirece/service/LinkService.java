package com.lulafreire.apsirece.service;

import com.lulafreire.apsirece.model.Link;
import com.lulafreire.apsirece.model.CategoriaLink;
import com.lulafreire.apsirece.repository.LinkRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor // O Lombok cria o construtor para injeção de dependência
public class LinkService {

    private final LinkRepository linkRepository;

    public List<Link> listarPorCategoria(CategoriaLink categoria) {
        return linkRepository.findByCategoria(categoria);
    }

    public List<Link> listarMaisVisitados() {
        return linkRepository.findTop8ByOrderByContadorCliquesDesc();
    }

    public void registrarClique(@NonNull Long id) {
        linkRepository.findById(id).ifPresent(link -> {
            link.setContadorCliques(link.getContadorCliques() + 1);
            linkRepository.save(link);
        });
    }
}