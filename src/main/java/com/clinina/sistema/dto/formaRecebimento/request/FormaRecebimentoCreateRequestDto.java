package com.clinina.sistema.dto.formaRecebimento.request;

import com.clinina.sistema.model.enums.TipoFormaRecebimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FormaRecebimentoCreateRequestDto(
        @NotBlank String nome,
        @NotNull TipoFormaRecebimento tipo
) {
}
