package com.clinina.sistema.dto.produto.response;

import java.util.List;

public record ProdutosDashboardResponseDto(
        List<ProdutoResponseDto> produtos,

        long produtosVencidos,
        long produtosVencendoEm60Dias
) {
}
