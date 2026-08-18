package com.clinina.sistema.dto.animal.response;

import com.clinina.sistema.model.enums.EspecieAnimal;

import java.math.BigDecimal;

public record AnimalPdvResponseDto(
        Long id,
        String nome,
        EspecieAnimal especie,
        String idade,
        BigDecimal peso
) {
}
