package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.*;
import com.clinina.sistema.model.entity.Grupo;
import com.clinina.sistema.model.entity.GrupoCategoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GrupoMapper {
    Grupo toEntity(GrupoCreateRequestDto dto);
    @Mapping(target = "grupoCategoriaId", source = "grupoCategoria.id")
    @Mapping(target = "grupoCategoriaNome", source = "grupoCategoria.nome")
    GrupoResponseDto toDto(Grupo grupo);

    GrupoCategoria toGrupoCategoria(GrupoCategoriaCreateRequestDto dto);
    GrupoCategoriaResponseDto toGrupoCategoriaDto(GrupoCategoria grupoCategoria);
}
