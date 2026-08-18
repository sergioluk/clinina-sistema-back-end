package com.clinina.sistema.dto.caixa.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CaixaDespesaCreateRequestDto(
        @NotNull Long funcionarioId,
        @NotNull Long caixaId,
        @NotNull Long categoriaId,
        @NotNull Long formaRecebimentoId,
        @NotNull BigDecimal valor,
        String descricao,
        String observacao
) {
}
