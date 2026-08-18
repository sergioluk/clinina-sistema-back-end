package com.clinina.sistema.dto.cliente.request;

import com.clinina.sistema.dto.animal.request.AnimalCreateRequestDto;
import com.clinina.sistema.model.enums.ComoConheceu;
import com.clinina.sistema.model.enums.Nacionalidade;
import com.clinina.sistema.model.enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ClienteCreateRequestDto(
        @NotBlank String nomeCompleto,
        @NotNull Nacionalidade nacionalidade,
        Sexo sexo,
        String cpf,
        String rg,
        LocalDate aniversario,
        ComoConheceu comoConheceu,
        String profissao,
        List<ClienteMarcacaoCreateRequestDto> marcacoes,
        String observacoes,
        List<ClienteContatoCreateRequestDto> contatos,
        ClienteEnderecoCreateRequestDto endereco,
        List<AnimalCreateRequestDto> animais
) {
}
