package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.cliente.request.ClienteContatoCreateRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteContatoResponseDto;
import com.clinina.sistema.model.entity.ClienteContato;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClienteContatoMapper {

    ClienteContato toEntity(ClienteContatoCreateRequestDto dto);

    @Mapping(target = "clienteId", source = "contato.cliente.id")
    ClienteContatoResponseDto toResponseDto(ClienteContato contato);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ClienteContatoCreateRequestDto dto, @MappingTarget ClienteContato contato);
}
