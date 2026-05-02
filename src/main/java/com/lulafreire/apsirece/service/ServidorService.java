package com.lulafreire.apsirece.service;

import com.lulafreire.apsirece.model.Servidor;
import com.lulafreire.apsirece.repository.ServidorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServidorService {

    private final ServidorRepository repository;

    public List<Servidor> listarTodos() {
        return repository.findAllByOrderByNomeCompletoAsc();
    }

    public List<Servidor> buscarAniversariantes() {
        int mesAtual = LocalDate.now().getMonthValue();
        return repository.findAniversariantesDoMes(mesAtual);
    }

    public List<Servidor> listarAfastados() {
        return repository.findByStatusAfastamentoNot("Ativo");
    }
}