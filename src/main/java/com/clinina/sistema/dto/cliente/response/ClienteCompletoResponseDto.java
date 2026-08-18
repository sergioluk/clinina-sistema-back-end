package com.clinina.sistema.dto.cliente.response;

import com.clinina.sistema.dto.animal.response.AnimalCompletoResponseDto;
import com.clinina.sistema.dto.cliente.request.ClienteContatoCreateRequestDto;
import com.clinina.sistema.dto.cliente.request.ClienteEnderecoCreateRequestDto;
import com.clinina.sistema.dto.cliente.request.ClienteMarcacaoCreateRequestDto;
import com.clinina.sistema.model.enums.ComoConheceu;
import com.clinina.sistema.model.enums.Nacionalidade;
import com.clinina.sistema.model.enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ClienteCompletoResponseDto(
        Long id,
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
        List<AnimalCompletoResponseDto> animais
) {
}
