package com.lulafreire.apsirece.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable()) // Desabilitado para facilitar testes locais
                                .authorizeHttpRequests(auth -> auth
                                                // 1. RECURSOS ESTÁTICOS: Devem ser os primeiros para o layout não
                                                // quebrar
                                                .requestMatchers("/css/**", "/chat/**", "/js/**", "/fonts/**", "/img/**",
                                                                "/webjars/**", "/ws/**",
                                                                "/favicon.ico")
                                                .permitAll()

                                                // 2. PÁGINAS PÚBLICAS: Acesso livre para visitantes
                                                .requestMatchers("/", "/login", "/error", "/mensagens", "/digital",
                                                                "/equipe")
                                                .permitAll()

                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login") // Garante que use sua página de login customizada
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/")
                                                .permitAll());

                return http.build();
        }
}