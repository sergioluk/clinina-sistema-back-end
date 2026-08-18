package com.clinina.sistema.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_despesa_categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class DespesaCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;
}
