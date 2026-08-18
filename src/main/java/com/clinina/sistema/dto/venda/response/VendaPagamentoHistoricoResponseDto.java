package com.clinina.sistema.dto.venda.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record VendaPagamentoHistoricoResponseDto(
        BigDecimal valor,
        Instant dataPagamento,
        String formaRecebimento,
        List<VendaPagamentoParcelaHistoricoDto> parcelas
) {
}
