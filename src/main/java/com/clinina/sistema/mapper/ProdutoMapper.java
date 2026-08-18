package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.*;
import com.clinina.sistema.dto.produto.request.ProdutoCreateRequestDto;
import com.clinina.sistema.dto.produto.response.ProdutoBuscaResponseDto;
import com.clinina.sistema.dto.produto.response.ProdutoDetalhesResponseDto;
import com.clinina.sistema.dto.produto.response.ProdutoResponseDto;
import com.clinina.sistema.model.entity.Produto;
import com.clinina.sistema.model.enums.SituacaoEstoque;
import com.clinina.sistema.model.enums.StatusValidade;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "grupo", ignore = true)
    Produto toEntity(ProdutoCreateRequestDto dto);

    @Mapping(target = "marcaId", source = "marca.id")
    @Mapping(target = "grupoId", source = "grupo.id")
    ProdutoCreateRequestDto toDtoCompleto(Produto produto);

    @Mapping(target = "marcaId", source = "produto.marca.id")
    @Mapping(target = "marcaNome", source = "produto.marca.nome")
    @Mapping(target = "grupoId", source = "produto.grupo.id")
    @Mapping(target = "grupoNome", source = "produto.grupo.nome")
    @Mapping(target = "tempoCadastro", expression = "java(tempoCadastro)")
    @Mapping(target = "situacaoEstoque", expression = "java(situacaoEstoque)")
    @Mapping(target = "statusValidade", expression = "java(statusValidade)")
    @Mapping(target = "markup", expression = "java(markup)")
    @Mapping(target = "diasVencendo", expression = "java(diasVencendo)")
    ProdutoDetalhesResponseDto toDetalhesDto(Produto produto, int tempoCadastro, SituacaoEstoque situacaoEstoque, StatusValidade statusValidade, int markup, int diasVencendo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProdutoFromDto(ProdutoCreateRequestDto dto, @MappingTarget Produto produto);

    @Mapping(target = "markup", expression = "java(markup)")
    @Mapping(target = "situacaoEstoque", expression = "java(situacaoEstoque)")
    @Mapping(target = "statusValidade", expression = "java(statusValidade)")
    ProdutoResponseDto toDto(Produto produto, int markup, SituacaoEstoque situacaoEstoque, StatusValidade statusValidade);

    //@Mapping(target = "grupoNome", source = "produto.grupo.nome")
    @Mapping(target = "ehFracionado", source = "produto.ehFracionado")
    @Mapping(target = "unidadeVenda", source = "produto.unidadeVenda")
    ProdutoBuscaResponseDto toProdutoBuscaResponseDto(Produto produto, List<StatusMensagemDTO> mensagens);


}
