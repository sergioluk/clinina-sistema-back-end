package com.clinina.sistema.dto.caixa.response;

import com.clinina.sistema.model.enums.StatusCaixa;

import java.math.BigDecimal;
import java.time.Instant;

public record CaixaResponseDto(
        Long id,
        String nome,
        Instant dataHoraAbertura,
        Instant dataHoraFechamento,
        StatusCaixa status,
        Long funcionarioId,
        String funcionarioNome,
        BigDecimal saldo
) {
}
