package com.clinina.sistema.dto.venda.response;

import com.clinina.sistema.model.enums.VendaStatus;

import java.math.BigDecimal;
import java.util.List;

public record LocalizarVendaResponseDto(
        Long codigo,
        String data,
        ClienteResumoDto cliente,
        List<AnimalResumoDto> animais,
        BigDecimal valorTotal,
        BigDecimal valorPago,
        VendaStatus status
) {
    public record ClienteResumoDto(Long id, String nome) {}
    public record AnimalResumoDto(Long id, String nome) {}
}
