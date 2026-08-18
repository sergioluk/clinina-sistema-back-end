package com.clinina.sistema.dto.venda.request;

import java.util.List;

public record VendaFinalizeCreateRequestDto(
        List<VendasPagamentosFinalizeRequestDto> pagamentos
) {
}
