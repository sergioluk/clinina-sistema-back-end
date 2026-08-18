package com.clinina.sistema.dto.venda.response;

import java.math.BigDecimal;
import java.time.Instant;

public record VendaPagamentoParcelaHistoricoDto(
        BigDecimal valor,
        Instant dataPagamento
) {
}
