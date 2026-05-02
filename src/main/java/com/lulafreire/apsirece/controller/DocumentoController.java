package com.lulafreire.apsirece.controller;

import com.lulafreire.apsirece.model.Documento;
import com.lulafreire.apsirece.service.DocumentoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/digital")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocumentoController {

    private final DocumentoService service;

    @GetMapping
    public List<Documento> listar() {
        return service.listarTodos();
    }

    @PostMapping("/upload")
    public void upload(@RequestParam("titulo") String titulo,
            @RequestParam("arquivo") MultipartFile arquivo) throws Exception {
        service.salvar(titulo, arquivo);
    }

    @GetMapping("/download/{nomeArquivo}")
    public ResponseEntity<Resource> baixarArquivo(@PathVariable String nomeArquivo) throws Exception {
        Resource arquivo = service.carregar(nomeArquivo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getFilename() + "\"")
                .body(arquivo);
    }
}