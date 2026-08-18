package com.clinina.sistema.security;

import com.clinina.sistema.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioCreateRequestDto(
        @NotBlank String email,
        @NotBlank String senha,
        @NotNull Role role,
        Long funcionarioId
) {
}
