package com.clinina.sistema.model.entity;

import com.clinina.sistema.model.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tb_caixa_movimentacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CaixaMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caixa_id", nullable = false)
    private Caixa caixa;

    @Column(nullable = false)
    private Instant dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caixa_destino_id")
    private Caixa caixaRelacionado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Funcionario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_recebimento_id")
    private FormaRecebimento formaRecebimento;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;
}