package com.clinina.sistema.model.entity;

import com.clinina.sistema.model.enums.TipoFormaRecebimento;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_formas_recebimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FormaRecebimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoFormaRecebimento tipo;

    @Column(nullable = false)
    private Boolean ativo = true;
}
