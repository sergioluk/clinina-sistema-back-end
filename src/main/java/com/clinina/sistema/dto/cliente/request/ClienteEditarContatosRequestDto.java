package com.clinina.sistema.dto.cliente.request;

import com.clinina.sistema.model.enums.TipoContato;

import java.util.List;

public record ClienteEditarContatosRequestDto(
        List<ContatoDto> contatos
) {
    public record ContatoDto(
            TipoContato tipoContato,
            String valor,
            Boolean whatsapp,
            String observacoes
    ) {}
}
