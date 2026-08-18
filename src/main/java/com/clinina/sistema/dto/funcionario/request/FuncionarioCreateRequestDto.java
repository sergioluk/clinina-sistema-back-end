package com.clinina.sistema.dto.funcionario.request;

import jakarta.validation.constraints.NotBlank;

public record FuncionarioCreateRequestDto(
        @NotBlank String nome
) {
}
