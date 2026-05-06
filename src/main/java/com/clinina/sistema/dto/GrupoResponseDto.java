package com.clinina.sistema.dto;

public record GrupoResponseDto(
        Long id,
        String nome,
        Long grupoCategoriaId,
        String grupoCategoriaNome
) {
}
