package com.clinina.sistema.dto.venda.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VendaPagamentoCreateRequestDto(
        @NotNull Long formaRecebimentoId,
        @NotNull BigDecimal valor,
        @NotNull Integer quantidadeParcelas
) {
}
