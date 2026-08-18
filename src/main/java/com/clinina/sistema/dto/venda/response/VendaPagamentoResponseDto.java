package com.clinina.sistema.dto.venda.response;

import com.clinina.sistema.model.enums.StatusPagamento;
import com.clinina.sistema.model.enums.TipoFormaRecebimento;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record VendaPagamentoResponseDto(
        Long id,
        Long formaRecebimentoId,
        String formaRecebimentoNome,
        TipoFormaRecebimento tipo,
        BigDecimal valor,
        Integer quantidadeParcelas,
        StatusPagamento status,
        Instant dataPagamento,
        List<VendaPagamentoParcelaResponseDto> parcelas
) {
}