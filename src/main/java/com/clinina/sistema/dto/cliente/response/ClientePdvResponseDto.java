package com.clinina.sistema.dto.cliente.response;

import com.clinina.sistema.dto.animal.response.AnimalBuscaClienteResponseDto;
import com.clinina.sistema.dto.animal.response.AnimalPdvResponseDto;

import java.math.BigDecimal;
import java.util.List;

public record ClientePdvResponseDto(
        Long id,
        String nomeCompleto,
        BigDecimal saldoDevedor,
        List<ClienteMarcacaoResponseDto> marcacoes,
        List<AnimalPdvResponseDto> animais
) {
}
