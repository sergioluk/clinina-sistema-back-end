package com.clinina.sistema.dto.produto.request;

import java.time.LocalDate;

public record ProdutoValidadeUpdateRequestDto(
        boolean controlaValidade,
        LocalDate dataValidade
) {
}
