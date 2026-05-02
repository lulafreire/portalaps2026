package com.lulafreire.apsirece.repository;

import com.lulafreire.apsirece.model.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServidorRepository extends JpaRepository<Servidor, Long> {

    // Lista todos os servidores por ordem alfabética
    List<Servidor> findAllByOrderByNomeCompletoAsc();

    // Busca aniversariantes do mês (JPQL)
    @Query("SELECT s FROM Servidor s WHERE MONTH(s.dataNascimento) = :mes ORDER BY DAY(s.dataNascimento)")
    List<Servidor> findAniversariantesDoMes(@Param("mes") int mes);

    // Lista quem está com status diferente de 'Ativo'
    List<Servidor> findByStatusAfastamentoNot(String status);

    // NOVOS MÉTODOS PARA O CHAT PORTAL APS

    // Busca servidores pelo nome ou email (para a barra de pesquisa do Aside)
    @Query("SELECT s FROM Servidor s WHERE s.nomeCompleto LIKE %:termo% OR s.usuario.email LIKE %:termo%")
    List<Servidor> searchServidores(@Param("termo") String termo);

    // Busca apenas servidores que estão online no momento
    @Query("SELECT s FROM Servidor s WHERE s.usuario.online = 1")
    List<Servidor> findServidoresOnline();

    // Busca um servidor específico pelo email do usuário (útil para abrir chat via
    // clique)
    Servidor findByUsuarioUsername(String username);
}