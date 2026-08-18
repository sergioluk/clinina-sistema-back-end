package com.clinina.sistema.dto.produto.request;

import com.clinina.sistema.model.enums.ProdutoProposito;
import com.clinina.sistema.model.enums.TipoControleDesconto;
import com.clinina.sistema.model.enums.TipoProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoCreateRequestDto(
        Long id,
        @NotNull @NotBlank String codigoDeBarras,
        @NotNull @NotBlank String nome,
        @NotNull @NotBlank String unidadeVenda,
        Long marcaId,
        Long grupoId,
        @NotNull TipoProduto tipo,
        @NotNull ProdutoProposito proposito,
        BigDecimal custo,
        @NotNull Integer markupDesejado,
        @NotNull BigDecimal preco,
        @NotNull boolean exibePreco,
        @NotNull boolean permiteAlterarPreco,
        @NotNull Boolean controlaEstoque,
        @NotNull boolean ehFracionado,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        Integer estoqueAtual,
        @NotNull Boolean controlaValidade,
        LocalDate dataValidade,
        @NotNull TipoControleDesconto tipoControleDesconto,
        Integer descontoMaximo,
        @NotNull Boolean banhoTosa,
        @NotNull Boolean clinica,
        @NotNull Boolean petshop
) {
}
