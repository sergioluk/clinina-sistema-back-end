package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.cliente.request.ClienteMarcacaoCreateRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteMarcacaoResponseDto;
import com.clinina.sistema.model.entity.ClienteMarcacao;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClienteMarcacaoMapper {

    ClienteMarcacao toEntity(ClienteMarcacaoCreateRequestDto dto);

    @Mapping(target = "clienteId", source = "marcacao.cliente.id")
    ClienteMarcacaoResponseDto toResponseDto(ClienteMarcacao marcacao);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ClienteMarcacaoCreateRequestDto dto, @MappingTarget ClienteMarcacao marcacao);
}
