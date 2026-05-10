package com.lulafreire.apsirece.repository;

import com.lulafreire.apsirece.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca por email para o login e identificação no chat
    Optional<Usuario> findByEmail(String email);

    // Busca por username (padrão Spring Security)
    Optional<Usuario> findByUsername(String username);

    // Verifica se o email já existe antes de cadastrar novo servidor
    Boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.online = 0")
    int resetarTodosOffline();

}
