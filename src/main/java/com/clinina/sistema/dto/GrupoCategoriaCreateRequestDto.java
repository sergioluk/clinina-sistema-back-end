package com.clinina.sistema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GrupoCategoriaCreateRequestDto(
        @NotNull @NotBlank String nome
) {
}
