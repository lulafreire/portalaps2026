package com.lulafreire.apsirece.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    @ManyToOne
    @JoinColumn(name = "remetente_id", nullable = false)
    private Usuario remetente;

    // Se nulo, a mensagem é para um grupo
    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;

    // Se nulo, a mensagem é privada (direta)
    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    private boolean lida = false;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio = LocalDateTime.now();
}