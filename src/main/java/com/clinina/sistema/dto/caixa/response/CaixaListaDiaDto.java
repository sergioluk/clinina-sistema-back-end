package com.clinina.sistema.dto.caixa.response;

import com.clinina.sistema.model.enums.StatusCaixa;

import java.time.Instant;

public record CaixaListaDiaDto(
        Long id,
        Instant dataHoraAbertura,
        Instant dataHoraFechamento,
        Instant dataHoraEncerramento,
        StatusCaixa status,
        FuncionarioResumoDto funcionario
) {
    public record FuncionarioResumoDto(
            Long id,
            String nome
    ) {}
}