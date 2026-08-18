package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.caixa.request.CaixaCreateRequestDto;
import com.clinina.sistema.dto.caixa.response.CaixaResponseDto;
import com.clinina.sistema.model.entity.Caixa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CaixaMapper {

    @Mapping(target = "funcionarioId", source = "funcionario.id")
    @Mapping(target = "funcionarioNome", source = "funcionario.nome")
    CaixaResponseDto toResponseDto(Caixa caixa);

    Caixa toEntity(CaixaCreateRequestDto dto);
}
