package com.clinina.sistema.dto.produto.response;

import com.clinina.sistema.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoDetalhesResponseDto(
        Long id,
        String codigoDeBarras,
        String nome,
        String unidadeVenda,
        Long marcaId,
        TipoProduto tipo,
        String marcaNome,
        Long grupoId,
        String grupoNome,
        ProdutoProposito proposito,
        BigDecimal custo,
        Integer markupDesejado,
        BigDecimal preco,
        boolean exibePreco,
        boolean permiteAlterarPreco,
        Boolean controlaEstoque,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        Integer estoqueAtual,
        Boolean controlaValidade,
        LocalDate dataValidade,
        TipoControleDesconto tipoControleDesconto,
        Integer descontoMaximo,
        Boolean banhoTosa,
        Boolean clinica,
        Boolean petshop,
        int tempoCadastro,
        SituacaoEstoque situacaoEstoque,
        StatusValidade statusValidade,
        int diasVencendo,
        int markup,
        boolean ehFracionado
) {
}
