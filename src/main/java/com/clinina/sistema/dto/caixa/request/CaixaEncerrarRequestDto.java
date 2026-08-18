package com.clinina.sistema.dto.caixa.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record CaixaEncerrarRequestDto(
        @NotNull Long funcionarioId,
        @NotNull Long contaDestinoId,
        @NotNull LocalDate data,
        @NotNull LocalTime hora,
        @Size(max = 255) String comentario
) {}