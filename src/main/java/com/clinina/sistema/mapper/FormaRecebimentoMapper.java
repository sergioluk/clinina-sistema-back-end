package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.formaRecebimento.request.FormaRecebimentoCreateRequestDto;
import com.clinina.sistema.dto.formaRecebimento.response.FormaRecebimentoResponseDto;
import com.clinina.sistema.model.entity.FormaRecebimento;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FormaRecebimentoMapper {

    FormaRecebimento toEntity(FormaRecebimentoCreateRequestDto dto);

    FormaRecebimentoResponseDto toResponseDto(FormaRecebimento formaRecebimento);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(
            FormaRecebimentoCreateRequestDto dto,
            @MappingTarget FormaRecebimento formaRecebimento
    );
}
