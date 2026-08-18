package com.clinina.sistema.dto.formaRecebimento.response;

import com.clinina.sistema.model.enums.TipoFormaRecebimento;

public record FormaRecebimentoResponseDto(
        Long id,
        String nome,
        TipoFormaRecebimento tipo,
        Boolean ativo
) {
}
