package com.clinina.sistema.dto.cliente.request;

public record ClienteEditarEnderecoRequestDto(
        String cep,
        String rua,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String pontoReferencia
) {}