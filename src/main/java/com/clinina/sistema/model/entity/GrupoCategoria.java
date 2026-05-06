package com.clinina.sistema.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "GrupoCategoria")
@Table(name = "tb_grupo_categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class GrupoCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "grupoCategoria")
    private List<Grupo> grupos;
}
