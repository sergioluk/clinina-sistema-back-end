package com.clinina.sistema.dto.cliente.response;

import java.util.List;

public record ClienteResumoVendaResponseDto(
        Long id,
        String nome,
        List<ClienteContatoResponseDto> contatos,
        ClienteEnderecoResponseDto endereco
) {
}
