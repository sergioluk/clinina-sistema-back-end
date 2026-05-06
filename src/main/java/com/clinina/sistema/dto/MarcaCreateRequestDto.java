package com.clinina.sistema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MarcaCreateRequestDto(
        @NotNull @NotBlank String nome
) {
}
