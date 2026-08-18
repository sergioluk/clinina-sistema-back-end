package com.clinina.sistema.dto.cliente.response;

import com.clinina.sistema.model.enums.TipoContato;

public record ClienteContatoResponseDto(
        Long id,
        Long clienteId,
        TipoContato tipoContato,
        String valor,
        Boolean whatsapp,
        String observacoes
) {
}
