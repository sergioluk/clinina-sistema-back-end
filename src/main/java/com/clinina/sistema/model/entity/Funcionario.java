package com.clinina.sistema.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_funcionarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "funcionario")
    private List<VendaItem> vendaItens = new ArrayList<>();

    @Column(nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
