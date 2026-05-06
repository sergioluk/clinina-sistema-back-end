package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.MarcaCreateRequestDto;
import com.clinina.sistema.dto.MarcaResponseDto;
import com.clinina.sistema.model.entity.Marca;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarcaMapper {

    Marca toEntity(MarcaCreateRequestDto dto);

    MarcaResponseDto toDto(Marca marca);
}
