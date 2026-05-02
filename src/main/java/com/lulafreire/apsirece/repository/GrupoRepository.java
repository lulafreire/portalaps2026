package com.lulafreire.apsirece.repository;

import com.lulafreire.apsirece.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    // Carrega todos os grupos com todos os membros (Para visão de Admin)
    @Query("SELECT DISTINCT g FROM Grupo g LEFT JOIN FETCH g.membros ORDER BY g.nome ASC")
    List<Grupo> findAllWithMembrosOrdered();

    // PASSO 1: Busca apenas os IDs dos grupos onde o usuário é membro
    @Query("SELECT g.id FROM Grupo g JOIN g.membros m WHERE m.username = :username")
    List<Long> findIdsByMembroUsername(@Param("username") String username);

    // PASSO 2: Carrega os grupos completos (com todos os membros) baseados nos IDs
    // encontrados
    @Query("SELECT DISTINCT g FROM Grupo g LEFT JOIN FETCH g.membros WHERE g.id IN :ids ORDER BY g.nome ASC")
    List<Grupo> findAllByIdInWithMembros(@Param("ids") List<Long> ids);

    List<Grupo> findByNomeContainingIgnoreCase(String nome);
}