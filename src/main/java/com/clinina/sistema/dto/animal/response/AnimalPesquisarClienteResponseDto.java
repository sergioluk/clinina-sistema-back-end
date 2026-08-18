package com.clinina.sistema.dto.animal.response;

public record AnimalPesquisarClienteResponseDto(
        Long animalId,
        String animalNome,
        Long clienteId,
        String clienteNome
) {
}
