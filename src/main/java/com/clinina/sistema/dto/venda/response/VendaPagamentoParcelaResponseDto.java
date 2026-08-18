package com.clinina.sistema.dto.venda.response;

import com.clinina.sistema.model.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record VendaPagamentoParcelaResponseDto(
        Long id,
        Integer numeroParcela,
        BigDecimal valor,
        LocalDate dataVencimento,
        Instant dataPagamento,
        StatusPagamento status
) {
}
