package com.clinina.sistema.dto.animal.request;

import com.clinina.sistema.model.enums.EspecieAnimal;
import com.clinina.sistema.model.enums.EsterilizacaoAnimal;
import com.clinina.sistema.model.enums.SexoAnimal;
import com.clinina.sistema.model.enums.StatusAnimal;

import java.time.LocalDate;
import java.util.List;

public record AnimalCreateRequestDto(
        Long clienteId,
        String nome,
        EspecieAnimal especie,
        SexoAnimal sexo,
        String raca,
        EsterilizacaoAnimal esterilizacao,
        LocalDate nascimento,
        String pelagem,
        StatusAnimal status,
        String fotoUrl,
        List<AnimalMarcacaoCreateRequestDto> marcacoes
) {
}
