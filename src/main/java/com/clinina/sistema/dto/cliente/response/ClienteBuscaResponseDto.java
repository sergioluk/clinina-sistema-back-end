package com.clinina.sistema.dto.cliente.response;

import com.clinina.sistema.dto.animal.response.AnimalBuscaClienteResponseDto;

import java.util.List;

public record ClienteBuscaResponseDto(
        Long id,
        String nomeCompleto,
        List<AnimalBuscaClienteResponseDto> animais
) {
}