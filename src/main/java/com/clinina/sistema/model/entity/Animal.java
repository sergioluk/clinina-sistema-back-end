package com.clinina.sistema.model.entity;

import com.clinina.sistema.model.enums.EspecieAnimal;
import com.clinina.sistema.model.enums.EsterilizacaoAnimal;
import com.clinina.sistema.model.enums.SexoAnimal;
import com.clinina.sistema.model.enums.StatusAnimal;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_animais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;


    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EspecieAnimal especie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SexoAnimal sexo;

    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EsterilizacaoAnimal esterilizacao;

    private LocalDate nascimento;

    private String pelagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAnimal status = StatusAnimal.VIVO;


    @Column(name = "foto_url")
    private String fotoUrl;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnimalMarcacao> marcacoes = new ArrayList<>();

    @Column(name = "peso", precision = 10, scale = 2)
    private BigDecimal peso;

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


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
