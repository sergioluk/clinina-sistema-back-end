package com.clinina.sistema.dto.produto.response;

import com.clinina.sistema.dto.StatusMensagemDTO;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoBuscaResponseDto(
        long id,
        String nome,
        BigDecimal preco,
        boolean ehFracionado,
        String unidadeVenda,
        List<StatusMensagemDTO> mensagens
) {
}
