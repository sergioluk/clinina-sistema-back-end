package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.funcionario.request.FuncionarioCreateRequestDto;
import com.clinina.sistema.dto.funcionario.response.FuncionarioAtivoResponseDto;
import com.clinina.sistema.dto.funcionario.response.FuncionarioResponseDto;
import com.clinina.sistema.model.entity.Funcionario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FuncionarioMapper {

    Funcionario toEntity(FuncionarioCreateRequestDto dto);

    FuncionarioResponseDto toResponseDto(Funcionario funcionario);

    FuncionarioAtivoResponseDto toAtivoResponseDto(Funcionario funcionario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(
            FuncionarioCreateRequestDto dto,
            @MappingTarget Funcionario funcionario
    );
}
