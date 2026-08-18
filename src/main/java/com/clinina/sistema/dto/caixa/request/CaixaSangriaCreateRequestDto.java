package com.clinina.sistema.dto.caixa.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CaixaSangriaCreateRequestDto(
        @NotNull Long funcionarioId,
        @NotNull Long caixaId,
        @NotNull Long contaId,
        @NotNull BigDecimal valor,
        String descricao
) {
}
