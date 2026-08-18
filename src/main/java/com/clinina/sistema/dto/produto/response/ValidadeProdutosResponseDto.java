package com.clinina.sistema.dto.produto.response;

import java.util.List;

public record ValidadeProdutosResponseDto(
        long quantidadeVencidos,
        List<ValidadeProdutosNomesIdsResponseDto> produtosVencidos,

        long quantidadeVencendoEm60Dias,
        List<ValidadeProdutosNomesIdsResponseDto> produtosVencendoEm60Dias
) {
}
