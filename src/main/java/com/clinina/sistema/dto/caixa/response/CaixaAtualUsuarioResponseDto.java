package com.clinina.sistema.dto.caixa.response;

public record CaixaAtualUsuarioResponseDto(
        CaixaResponseDto caixa,
        boolean precisaFinalizar,
        String acaoDisponivel
) {
}
