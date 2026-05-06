package com.clinina.sistema.dto;

import com.clinina.sistema.model.enums.TipoControleDesconto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoCreateRequestDto(
        Long id,
        @NotNull @NotBlank String codigoDeBarras,
        @NotNull @NotBlank String nome,
        @NotNull @NotBlank String unidadeVenda,
        Long marcaId,
        Long grupoId,
        BigDecimal custo,
        @NotNull BigDecimal preco,
        @NotNull Boolean controlaEstoque,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        Integer estoqueAtual,
        @NotNull Boolean controlaValidade,
        LocalDateTime dataValidade,
        @NotNull TipoControleDesconto tipoControleDesconto,
        Integer descontoMaximo,
        @NotNull Boolean banhoTosa,
        @NotNull Boolean clinica,
        @NotNull Boolean petshop
) {
}
