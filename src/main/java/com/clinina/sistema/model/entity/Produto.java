package com.clinina.sistema.model.entity;

import com.clinina.sistema.model.enums.ProdutoProposito;
import com.clinina.sistema.model.enums.TipoControleDesconto;
import com.clinina.sistema.model.enums.TipoProduto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

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

    @Column(name = "proposito", nullable = false)
    private ProdutoProposito proposito;
    @Column(name = "markup_desejado", nullable = false)
    private Integer markupDesejado;
    @Column(name = "custo")
    private BigDecimal custo;
    @Column(name = "preco", nullable = false)
    private BigDecimal preco;
    @Column(name = "exibe_preco", nullable = false)
    private boolean exibePreco;
    @Column(name = "permite_alterar_preco", nullable = false)
    private boolean permiteAlterarPreco;

    @Column(name = "controla_estoque", nullable = false)
    private Boolean controlaEstoque;
    @Column(name = "estoque_minimo", precision = 10, scale = 2)
    private BigDecimal estoqueMinimo;
    @Column(name = "estoque_maximo", precision = 10, scale = 2)
    private BigDecimal estoqueMaximo;
    @Column(name = "estoque_atual", precision = 10, scale = 2)
    private BigDecimal estoqueAtual;

    @Column(name = "controla_validade", nullable = false)
    private Boolean controlaValidade;
    @Column(name = "data_validade")
    private LocalDate dataValidade;

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

    @Column(name = "eh_fracionado", nullable = false)
    private Boolean ehFracionado = false;

    @Column(name = "tipo_produto", nullable = false)
    private TipoProduto tipo;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;


    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "nome_normalizado")
    private String nomeNormalizado;

    @PrePersist
    @PreUpdate
    public void normalizarNome() {
        if (this.nome != null) {
            this.nomeNormalizado = normalize(this.nome);
        }
    }

    private String normalize(String texto) {
        return java.text.Normalizer
                .normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .toLowerCase()
                .trim();
    }
}
