package com.lulafreire.apsirece.service;

import com.lulafreire.apsirece.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca na tb_usuarios e trata a possibilidade de o usuário não existir
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " não localizado no banco."));
    }
}