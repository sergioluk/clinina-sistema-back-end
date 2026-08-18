package com.clinina.sistema.dto.cliente.response;

import com.clinina.sistema.dto.animal.response.AnimalCompletoResponseDto;
import com.clinina.sistema.model.enums.ComoConheceu;
import com.clinina.sistema.model.enums.Nacionalidade;
import com.clinina.sistema.model.enums.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ClientePerfilResponseDto(
        Long id,
        @NotBlank String nomeCompleto,
        @NotNull Nacionalidade nacionalidade,
        Sexo sexo,
        String cpf,
        String rg,
        LocalDate aniversario,
        ComoConheceu comoConheceu,
        String profissao,
        BigDecimal saldo,
        List<ClienteMarcacaoResponseDto> marcacoes,
        String observacoes,
        List<ClienteContatoResponseDto> contatos,
        ClienteEnderecoResponseDto endereco,
        List<AnimalCompletoResponseDto> animais
) {
}
