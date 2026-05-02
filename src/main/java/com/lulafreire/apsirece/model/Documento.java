package com.lulafreire.apsirece.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_documentos")
@Getter // Resolve os erros de 'undefined method' no Service
@Setter // Resolve os erros de 'undefined method' no Service
@NoArgsConstructor
@AllArgsConstructor
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String path;

    private LocalDate dataCriacao;
}