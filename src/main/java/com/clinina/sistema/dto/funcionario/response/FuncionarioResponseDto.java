package com.clinina.sistema.dto.funcionario.response;

public record FuncionarioResponseDto(
        Long id,
        String nome,
        Boolean ativo
) {
}
