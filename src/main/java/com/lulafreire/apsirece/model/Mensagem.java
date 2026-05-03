package com.lulafreire.apsirece.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tb_mensagens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    // Relacionamento com quem enviou
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remetente_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "password", "authorities" })
    private Usuario remetente;

    // Se nulo, a mensagem é para um grupo
    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;

    // Se nulo, a mensagem é privada (direta)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    @JsonIgnoreProperties({ "membros", "hibernateLazyInitializer", "handler" })
    private Grupo grupo;

    private boolean lida = false;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio = LocalDateTime.now();
}