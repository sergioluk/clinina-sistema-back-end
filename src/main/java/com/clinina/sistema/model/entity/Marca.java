package com.clinina.sistema.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Marca")
@Table(name = "tb_marcas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "marca")
    private List<Produto> produtos;
}
