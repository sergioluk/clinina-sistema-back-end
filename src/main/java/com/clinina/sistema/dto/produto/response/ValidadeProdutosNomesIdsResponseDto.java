package com.clinina.sistema.dto.produto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ValidadeProdutosNomesIdsResponseDto(
        Long id,
        String nome,
        LocalDate dataValidade,
        BigDecimal estoqueAtual
) {
}
