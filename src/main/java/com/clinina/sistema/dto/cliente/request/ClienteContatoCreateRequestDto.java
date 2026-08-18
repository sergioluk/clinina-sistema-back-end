package com.clinina.sistema.dto.cliente.request;

import com.clinina.sistema.model.enums.TipoContato;

public record ClienteContatoCreateRequestDto(
        TipoContato tipoContato,
        String valor,
        Boolean whatsapp,
        String observacoes
) {
}
