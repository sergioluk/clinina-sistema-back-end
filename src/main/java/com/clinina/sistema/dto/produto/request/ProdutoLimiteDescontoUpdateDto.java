package com.clinina.sistema.dto.produto.request;

import com.clinina.sistema.model.enums.TipoControleDesconto;

public record ProdutoLimiteDescontoUpdateDto(
        TipoControleDesconto tipoControleDesconto,
        Integer descontoMaximo
) {
}
