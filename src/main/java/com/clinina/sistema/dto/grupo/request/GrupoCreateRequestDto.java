package com.clinina.sistema.dto.grupo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GrupoCreateRequestDto(
        @NotNull @NotBlank String nome,
        @NotNull Long grupoCategoriaId
) {
}
