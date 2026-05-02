package com.lulafreire.apsirece.repository;

import com.lulafreire.apsirece.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    // Usado pelo método carregar()
    Documento findByNome(String nome);

    // Usado pelo IndexController
    List<Documento> findByDataCriacao(LocalDate data);
}