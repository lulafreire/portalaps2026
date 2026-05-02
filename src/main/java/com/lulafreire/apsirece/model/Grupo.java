package com.lulafreire.apsirece.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tb_grupos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "criado_por_id")
    private Usuario criador;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    // Relacionamento Many-to-Many utilizando Set para evitar duplicidade de membros
    // O nome da tabela de junção deve coincidir com o que está no seu MySQL
    // (tb_grupo_membros)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_grupo_membros", joinColumns = @JoinColumn(name = "grupo_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> membros;

    /**
     * Retorna a quantidade de membros no grupo para exibição na interface.
     * Utilizado no principal.html para mostrar o badge com o número de integrantes.
     */
    public int getQuantidadeMembros() {
        return (membros != null) ? membros.size() : 0;
    }

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}