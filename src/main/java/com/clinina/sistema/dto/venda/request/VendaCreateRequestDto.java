package com.clinina.sistema.dto.venda.request;

import java.math.BigDecimal;
import java.util.List;

public record VendaCreateRequestDto(
        Long clienteId,
        Long caixaId,
        List<VendaItemCreateRequestDto> itens,
        List<VendaPagamentoCreateRequestDto> pagamentos
) {
}
