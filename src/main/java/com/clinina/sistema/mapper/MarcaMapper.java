package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.marca.request.MarcaCreateRequestDto;
import com.clinina.sistema.dto.marca.response.MarcaResponseDto;
import com.clinina.sistema.model.entity.Marca;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarcaMapper {

    Marca toEntity(MarcaCreateRequestDto dto);

    MarcaResponseDto toDto(Marca marca);
}
