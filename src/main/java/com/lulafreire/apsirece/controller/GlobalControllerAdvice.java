package com.lulafreire.apsirece.controller;

import com.lulafreire.apsirece.model.Usuario;
import com.lulafreire.apsirece.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UsuarioRepository usuarioRepository;

    @ModelAttribute("estaLogado")
    public boolean estaLogado(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails != null;
    }

    @ModelAttribute("usuario")
    public Usuario usuarioLogado(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null)
            return null;
        return usuarioRepository.findByUsername(userDetails.getUsername()).orElse(null);
    }
}