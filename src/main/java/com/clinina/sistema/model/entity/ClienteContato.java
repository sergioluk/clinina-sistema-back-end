package com.clinina.sistema.model.entity;

import com.clinina.sistema.model.enums.TipoContato;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_cliente_contatos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ClienteContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contato", nullable = false)
    private TipoContato tipoContato;

    @Column(nullable = false)
    private String valor;

    @Column(name = "whatsapp")
    private Boolean whatsapp;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
