package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.venda.response.VendaItemResponseDto;
import com.clinina.sistema.dto.venda.response.VendaPagamentoParcelaResponseDto;
import com.clinina.sistema.dto.venda.response.VendaPagamentoResponseDto;
import com.clinina.sistema.dto.venda.response.VendaResponseDto;
import com.clinina.sistema.model.entity.Venda;
import com.clinina.sistema.model.entity.VendaItem;
import com.clinina.sistema.model.entity.VendaPagamento;
import com.clinina.sistema.model.entity.VendaPagamentoParcela;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VendaMapper {

    @Mapping(target = "cliente.id", source = "cliente.id")
    @Mapping(target = "caixa.caixaId", source = "caixa.id")
    @Mapping(target = "caixa.funcionarioNome", source = "caixa.funcionario.nome")
    @Mapping(target = "cliente.nome", source = "cliente.nomeCompleto")
    VendaResponseDto toResponseDto(Venda venda);

    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "animalId", source = "animal.id")
    @Mapping(target = "animalNome", source = "animal.nome")
    @Mapping(target = "funcionarioId", source = "funcionario.id")
    @Mapping(target = "funcionarioNome", source = "funcionario.nome")
    @Mapping(target = "ehFracionado", source = "produto.ehFracionado")
    @Mapping(target = "unidadeVenda", source = "produto.unidadeVenda")
    VendaItemResponseDto toItemResponseDto(VendaItem item);

    @Mapping(target = "formaRecebimentoId", source = "formaRecebimento.id")
    @Mapping(target = "formaRecebimentoNome", source = "formaRecebimento.nome")
    @Mapping(target = "tipo", source = "formaRecebimento.tipo")
    VendaPagamentoResponseDto toPagamentoResponseDto(VendaPagamento pagamento);

    VendaPagamentoParcelaResponseDto toParcelaResponseDto(VendaPagamentoParcela parcela);
}
