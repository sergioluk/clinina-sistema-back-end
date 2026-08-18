package com.clinina.sistema.dto.cliente.request;

import com.clinina.sistema.model.enums.ComoConheceu;
import com.clinina.sistema.model.enums.Nacionalidade;
import com.clinina.sistema.model.enums.Sexo;

import java.util.List;

public record ClienteEditarDadosGeraisRequestDto(
        String nomeCompleto,
        Nacionalidade nacionalidade,
        Sexo sexo,
        String cpf,
        String rg,
        String aniversario,
        ComoConheceu comoNosConheceu,
        List<ClienteMarcacaoCreateRequestDto> marcacoes,
        String observacoes
) {
}
