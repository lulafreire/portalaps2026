package com.lulafreire.apsirece.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_servidores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Servidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // VÍNCULO COM A TABELA DE USUÁRIOS
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String nomeCompleto;
    private String matricula;
    private String cargo;

    // Gestão de Afastamentos
    private String statusAfastamento;
    private LocalDate dataInicioAfastamento;
    private LocalDate dataFimAfastamento;
    private LocalDate dataNascimento;
}