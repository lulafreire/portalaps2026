package com.lulafreire.apsirece.service;

import com.lulafreire.apsirece.model.Documento;
import com.lulafreire.apsirece.repository.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository documentoRepository;

    // Define o caminho onde os arquivos serão salvos no servidor
    private final Path root = Paths.get("uploads");

    /**
     * Resolve o erro: salvar(String, MultipartFile) is undefined
     */
    public void salvar(String nome, MultipartFile arquivo) throws IOException {
        // Cria a pasta se não existir
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        // Salva o arquivo fisicamente
        String nomeArquivoOriginal = arquivo.getOriginalFilename();
        Files.copy(arquivo.getInputStream(), this.root.resolve(nomeArquivoOriginal));

        // Salva os metadados no banco (Porta 3307)
        Documento doc = new Documento();
        doc.setNome(nome);
        doc.setPath(nomeArquivoOriginal);
        doc.setDataCriacao(LocalDate.now());

        documentoRepository.save(doc);
    }

    /**
     * Resolve o erro: carregar(String) is undefined
     * Retorna um Resource para que o Controller possa processar o download/exibição
     */
    public Resource carregar(String nome) throws MalformedURLException {
        Documento doc = documentoRepository.findByNome(nome);
        Path arquivo = root.resolve(doc.getPath());
        @SuppressWarnings("null")
        Resource resource = new UrlResource(arquivo.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Não foi possível ler o arquivo!");
        }
    }

    // Métodos necessários para o IndexController
    public List<Documento> buscarPorData(LocalDate data) {
        return documentoRepository.findByDataCriacao(data);
    }

    public long contarDocumentos() {
        return documentoRepository.count();
    }

    public List<Documento> listarTodos() {
        return documentoRepository.findAll();
    }
}