package com.clinina.sistema.dto.produto.request;

import com.clinina.sistema.model.enums.TipoProduto;

public record ProdutoDadosBasicosUpdateRequestDto(
        TipoProduto tipoProduto,
        String codigoDeBarras,
        String nome,
        String unidadeVenda,
        Long marcaId,
        Long grupoId,
        boolean banhoTosa,
        boolean clinica,
        boolean petshop
) {
}
