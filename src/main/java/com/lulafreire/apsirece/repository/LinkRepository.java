package com.lulafreire.apsirece.repository;

import com.lulafreire.apsirece.model.Link;
import com.lulafreire.apsirece.model.CategoriaLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    // Retorna os links de uma categoria específica (para as abas da Home)
    List<Link> findByCategoria(CategoriaLink categoria);

    // Retorna os 8 links mais clicados (para a barra lateral)
    // O Spring gera o SQL: SELECT * FROM tb_links ORDER BY contador_cliques DESC
    // LIMIT 8
    List<Link> findTop8ByOrderByContadorCliquesDesc();
}
