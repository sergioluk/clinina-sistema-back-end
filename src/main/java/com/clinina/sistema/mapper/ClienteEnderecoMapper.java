package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.cliente.request.ClienteEnderecoCreateRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteEnderecoResponseDto;
import com.clinina.sistema.model.entity.ClienteEndereco;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClienteEnderecoMapper {

    ClienteEndereco toEntity(ClienteEnderecoCreateRequestDto dto);

    @Mapping(target = "clienteId", source = "endereco.cliente.id")
    ClienteEnderecoResponseDto toResponseDto(ClienteEndereco endereco);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ClienteEnderecoCreateRequestDto dto, @MappingTarget ClienteEndereco endereco);
}
