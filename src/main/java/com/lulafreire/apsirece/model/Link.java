package com.lulafreire.apsirece.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tb_links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String url;
    private String descricao;

    @Column(name = "icone_classe")
    private String iconeClasse; // Ex: "ph-light ph-calculator"

    @Enumerated(EnumType.STRING)
    private CategoriaLink categoria;

    private Long contadorCliques = 0L;

    public void setIconeClasse(String iconeClasse) {
        this.iconeClasse = iconeClasse;
    }

    // Faça o mesmo para a Descrição
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}