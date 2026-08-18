package com.clinina.sistema.model.entity;

import com.clinina.sistema.model.enums.StatusCaixa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tb_caixas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "data_hora_abertura", nullable = false)
    private Instant dataHoraAbertura;

    @Column(name = "data_hora_fechamento")
    private Instant dataHoraFechamento;

    @Column(name = "data_hora_encerramento")
    private Instant dataHoraEncerramento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCaixa status = StatusCaixa.ABERTO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @Column(name = "comentario")
    private String comentario;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
