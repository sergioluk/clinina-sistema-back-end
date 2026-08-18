package com.clinina.sistema.security;

public record LoginResponseDto(
        String token,
        String tipoToken,
        FuncionarioResumoDto funcionario
) {

    public record FuncionarioResumoDto(
            Long id,
            String nome
    ) {}
}