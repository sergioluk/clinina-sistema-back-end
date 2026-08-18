package com.clinina.sistema.dto.grupo.response;

public record GrupoResponseDto(
        Long id,
        String nome,
        Long grupoCategoriaId,
        String grupoCategoriaNome
) {
}
