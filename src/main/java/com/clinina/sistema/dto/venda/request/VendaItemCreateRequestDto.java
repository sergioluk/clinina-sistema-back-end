package com.clinina.sistema.dto.venda.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VendaItemCreateRequestDto(
        Long produtoId,
        Long animalId,
        Long funcionarioId,
        @NotNull BigDecimal quantidade,
        @NotNull BigDecimal valorUnitario,
        BigDecimal valorDesconto
) {
}
