package com.clinina.sistema.dto.venda.response;

import com.clinina.sistema.dto.caixa.response.CaixaResumoVendaDto;
import com.clinina.sistema.dto.cliente.response.ClienteContatoResponseDto;
import com.clinina.sistema.dto.cliente.response.ClienteResumoVendaResponseDto;
import com.clinina.sistema.model.enums.VendaStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record VendaResponseDto(
        Long id,
        Instant dataHoraVenda,
        BigDecimal valorBruto,
        BigDecimal valorDesconto,
        BigDecimal valorTotal,
        VendaStatus status,
        String observacoes,
        ClienteResumoVendaResponseDto cliente,
        CaixaResumoVendaDto caixa,
        List<VendaItemResponseDto> itens,
        List<VendaPagamentoResponseDto> pagamentos
) {
}
