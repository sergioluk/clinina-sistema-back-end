package com.clinina.sistema.dto;

import com.clinina.sistema.model.enums.SituacaoEstoque;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponseDto(
        Long id,
        String nome,
        String codigoDeBarras,
        LocalDateTime dataValidade,
        Integer estoqueAtual,

        BigDecimal custo,
        int markup,
        BigDecimal preco,
        SituacaoEstoque situacaoEstoque
) {
}
