package com.clinina.sistema.dto.venda.response;

import java.math.BigDecimal;

public record VendaItemResponseDto(
        Long id,
        Long produtoId,
        String descricao,
        Long animalId,
        String animalNome,
        Long funcionarioId,
        String funcionarioNome,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorDesconto,
        BigDecimal valorTotal,
        boolean ehFracionado,
        String unidadeVenda
) {
}
