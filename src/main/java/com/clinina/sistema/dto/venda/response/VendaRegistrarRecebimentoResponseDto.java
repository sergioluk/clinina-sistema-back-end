package com.clinina.sistema.dto.venda.response;

import java.math.BigDecimal;
import java.time.Instant;

public record VendaRegistrarRecebimentoResponseDto(
        Long id,
        Instant dataHoraVenda,
        BigDecimal valorTotal
) {
}
