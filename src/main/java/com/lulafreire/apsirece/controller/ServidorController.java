package com.lulafreire.apsirece.controller;

import com.lulafreire.apsirece.model.Servidor;
import com.lulafreire.apsirece.service.ServidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipe")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServidorController {

    private final ServidorService service;

    @GetMapping
    public List<Servidor> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/aniversariantes")
    public List<Servidor> aniversariantes() {
        return service.buscarAniversariantes();
    }

    @GetMapping("/afastados")
    public List<Servidor> afastados() {
        return service.listarAfastados();
    }
}