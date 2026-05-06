package com.clinina.sistema.model.entity;

import com.clinina.sistema.model.enums.SituacaoEstoque;
import com.clinina.sistema.model.enums.TipoControleDesconto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity(name = "Produto")
@Table(name = "tb_produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "codigo_barras", nullable = false, unique = true)
    private String codigoDeBarras;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "unidade_venda", nullable = false)
    private String unidadeVenda;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;
    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    @Column(name = "custo")
    private BigDecimal custo;
    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @Column(name = "controla_estoque", nullable = false)
    private Boolean controlaEstoque;
    @Column(name = "estoque_minimo")
    private Integer estoqueMinimo;
    @Column(name = "estoque_maximo")
    private Integer estoqueMaximo;
    @Column(name = "estoque_atual")
    private Integer estoqueAtual;

    @Column(name = "controla_validade", nullable = false)
    private Boolean controlaValidade;
    @Column(name = "data_validade")
    private LocalDateTime dataValidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_controle_desconto", nullable = false)
    private TipoControleDesconto tipoControleDesconto;
    @Column(name = "desconto_maximo")
    private Integer descontoMaximo;

    @Column(name = "banho_tosa", nullable = false)
    private Boolean banhoTosa;
    @Column(name = "clinica", nullable = false)
    private Boolean clinica;
    @Column(name = "petshop", nullable = false)
    private Boolean petshop;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public int calcularMarkup() {
        if (custo == null || preco == null || custo.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        return preco.subtract(custo)
                .divide(custo, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .intValue();
    }

    public SituacaoEstoque calcularSituacaoEstoque() {

        if (estoqueAtual == null) return null;

        if (estoqueAtual == 0) return SituacaoEstoque.PARADO;

        if (estoqueMinimo != null && estoqueAtual <= estoqueMinimo) {
            return SituacaoEstoque.REPOR;
        }

        if (estoqueMaximo != null && estoqueAtual > estoqueMaximo) {
            return SituacaoEstoque.EXCESSO;
        }

        return SituacaoEstoque.ADEQUADO;
    }
}
