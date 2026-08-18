package com.clinina.sistema.dto.venda.request;

import com.clinina.sistema.model.enums.VendaStatus;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record AtualizarStatusVendaRequestDto(
        @NotNull VendaStatus status,
        @NotNull List<VendaItemDto> itens
) {

    public record VendaItemDto(
            @NotNull Long produtoId,
            Long animalId, // opcional
            @NotNull Long funcionarioId,
            @NotNull BigDecimal quantidade,
            @NotNull BigDecimal valorUnitario,
            @NotNull BigDecimal valorDesconto
    ) {}
}