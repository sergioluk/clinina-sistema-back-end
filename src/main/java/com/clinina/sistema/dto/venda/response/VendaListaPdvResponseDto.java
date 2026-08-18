package com.clinina.sistema.dto.venda.response;

import com.clinina.sistema.model.enums.VendaStatus;

import java.math.BigDecimal;
import java.util.List;

public record VendaListaPdvResponseDto(
        long id,
        String nomeCliente,
        List<String> nomesPets,
        BigDecimal valorTotal,
        VendaStatus status
) {
}
