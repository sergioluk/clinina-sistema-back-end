package com.clinina.sistema.dto.produto.request;

import java.math.BigDecimal;

public record ProdutoEstoqueUpdateRequestDto(
        boolean controlaEstoque,
        boolean ehFracionado,
        BigDecimal minimo,
        BigDecimal maximo,
        BigDecimal estoque
) {
}
