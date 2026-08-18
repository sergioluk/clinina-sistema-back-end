package com.clinina.sistema.dto;

import com.clinina.sistema.model.enums.TipoMensagem;

public record StatusMensagemDTO(
        TipoMensagem tipo,
        String mensagem
) {
}
