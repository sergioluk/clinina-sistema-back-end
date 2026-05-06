package com.clinina.sistema.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Grupo")
@Table(name = "tb_grupos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "grupo_categoria_id")
    private GrupoCategoria grupoCategoria;
}
