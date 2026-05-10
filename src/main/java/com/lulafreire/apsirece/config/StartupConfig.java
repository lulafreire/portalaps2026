package com.lulafreire.apsirece.config;

import com.lulafreire.apsirece.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupConfig {

    private final UsuarioRepository usuarioRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void resetarStatusOnline() {
        int atualizados = usuarioRepository.resetarTodosOffline();
        log.info("Startup: {} usuário(s) marcado(s) como offline.", atualizados);
    }
}