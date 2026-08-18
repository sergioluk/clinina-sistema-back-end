package com.clinina.sistema.dto.caixa.request;

import com.clinina.sistema.model.entity.FormaRecebimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CaixaCreateRequestDto(
        @NotBlank String nome,
        @NotNull Long funcionarioId,
        Long contaOrigemId,
        BigDecimal valorSuprimento,
        String descricaoSuprimento,
        Long formaRecebimentoId
) {
}
