package com.clinina.sistema.dto.produto.response;

import com.clinina.sistema.model.enums.SituacaoEstoque;
import com.clinina.sistema.model.enums.StatusValidade;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoResponseDto(
        Long id,
        String nome,
        String codigoDeBarras,
        LocalDate dataValidade,
        BigDecimal estoqueAtual,

        BigDecimal custo,
        int markup,
        BigDecimal preco,

        SituacaoEstoque situacaoEstoque,
        StatusValidade statusValidade
) {
}
