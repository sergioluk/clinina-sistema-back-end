package com.clinina.sistema.dto.cliente.response;

public record ClienteEnderecoResponseDto(
        Long id,
        Long clienteId,
        String cep,
        String rua,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String pontoReferencia
) {
}
