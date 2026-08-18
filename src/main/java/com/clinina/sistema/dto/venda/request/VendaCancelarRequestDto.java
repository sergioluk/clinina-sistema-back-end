package com.clinina.sistema.dto.venda.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VendaCancelarRequestDto(

        @NotBlank
        String motivo,

        @NotNull
        Long funcionarioId

) {
}
