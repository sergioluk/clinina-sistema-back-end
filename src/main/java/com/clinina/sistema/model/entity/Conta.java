package com.clinina.sistema.model.entity;

import com.clinina.sistema.model.enums.SituacaoConta;
import com.clinina.sistema.model.enums.TipoConta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Entity
@Table(name = "tb_contas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConta tipo;

    @Column(nullable = false)
    private Boolean status = true; // ativo/inativo

    @Column(nullable = false)
    private Boolean permitirLancamentosRapidos = false;

    @Column(nullable = false)
    private LocalDate dataCriacao = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoConta situacao = SituacaoConta.POSITIVO;
}
