package com.clinina.sistema.dto.produto.response;

import java.util.List;

public record ProdutoPageResponseDto(
        List<ProdutoResponseDto> produtos,
        int paginaAtual,
        int totalPaginas,
        long totalElementos,
        int tamanhoPagina
) {
}