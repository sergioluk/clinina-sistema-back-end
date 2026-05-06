package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.ProdutoCreateRequestDto;
import com.clinina.sistema.dto.ProdutoResponseDto;
import com.clinina.sistema.model.entity.Produto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "grupo", ignore = true)
    Produto toEntity(ProdutoCreateRequestDto dto);

    @Mapping(target = "markup", expression = "java(produto.calcularMarkup())")
    @Mapping(target = "situacaoEstoque", expression = "java(produto.calcularSituacaoEstoque())")
    ProdutoResponseDto toDto(Produto produto);

    @Mapping(target = "marcaId", source = "marca.id")
    @Mapping(target = "grupoId", source = "grupo.id")
    ProdutoCreateRequestDto toDtoCompleto(Produto produto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProdutoFromDto(ProdutoCreateRequestDto dto, @MappingTarget Produto produto);


}
