package com.clinina.sistema.dto.conta.response;

import com.clinina.sistema.model.enums.SituacaoConta;
import com.clinina.sistema.model.enums.TipoConta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaResponseDto(
        Long id,
        String nome,
        TipoConta tipo,
        Boolean status,
        Boolean permitirLancamentosRapidos,
        LocalDate dataCriacao,
        BigDecimal saldo,
        SituacaoConta situacao
) {}
