package com.clinina.sistema.dto.venda.request;

import com.clinina.sistema.model.enums.VendaStatus;

public record LocalizarVendaRequestDto(
        Long codigo,
        Long clienteId,
        String data,
        VendaStatus status
) {
}
