package com.clinina.sistema.dto.venda.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaCreateOpenRequestDto(
        Long clienteId,
        Long caixaId,
        BigDecimal valorDesconto,
        String observacoes,
        LocalDateTime dataHoraVenda,
        List<VendaItemCreateRequestDto> itens
) {
}
