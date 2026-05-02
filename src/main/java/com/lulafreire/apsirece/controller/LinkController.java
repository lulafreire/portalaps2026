package com.lulafreire.apsirece.controller;

import com.lulafreire.apsirece.model.Link;
import com.lulafreire.apsirece.model.CategoriaLink;
import com.lulafreire.apsirece.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite que o seu Front-end aceda à API sem erros de CORS
public class LinkController {

    private final LinkService linkService;

    // Retorna os links filtrados por categoria (Abas: SISTEMA, FORMULARIO, etc)
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Link>> listarPorCategoria(@PathVariable CategoriaLink categoria) {
        return ResponseEntity.ok(linkService.listarPorCategoria(categoria));
    }

    // Retorna os 8 mais visitados para a barra lateral
    @GetMapping("/mais-visitados")
    public ResponseEntity<List<Link>> listarMaisVisitados() {
        return ResponseEntity.ok(linkService.listarMaisVisitados());
    }

    // Endpoint para quando o utilizador clica num link, incrementar o contador
    @PostMapping("/{id}/clique")
    public ResponseEntity<Void> registrarClique(@PathVariable @NonNull Long id) {
        linkService.registrarClique(id);
        return ResponseEntity.ok().build();
    }
}