package com.clinina.sistema.dto.produto.request;

import com.clinina.sistema.model.enums.ProdutoProposito;

import java.math.BigDecimal;

public record ProdutoCustoPrecoUpdateRequestDto(
        ProdutoProposito proposito,
        BigDecimal custo,
        Integer markupDesejado,
        BigDecimal preco,
        boolean exibePreco,
        boolean permiteAlterarPreco
) {
}
