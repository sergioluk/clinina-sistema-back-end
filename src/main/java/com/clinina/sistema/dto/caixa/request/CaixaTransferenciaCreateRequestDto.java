package com.clinina.sistema.dto.caixa.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CaixaTransferenciaCreateRequestDto(
        @NotNull Long funcionarioId,
        @NotNull Long caixaId,
        @NotNull Long caixaDestinoId,
        @NotNull BigDecimal valor
) {
}
