package com.clinina.sistema.dto.caixa.response;

import com.clinina.sistema.model.enums.StatusCaixa;
import com.clinina.sistema.model.enums.TipoMovimentacao;
import com.clinina.sistema.model.enums.VendaStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CaixaDetalheResponseDto(
        Long id,
        Instant dataHoraAbertura,
        Instant dataHoraFechamento,
        Instant dataHoraEncerramento,
        StatusCaixa status,
        FuncionarioResumoDto funcionario,
        List<CaixaResumoRecebimentoDto> resumoRecebimentos,
        List<CaixaMovimentacaoDto> movimentacoes,
        List<CaixaRecebimentoVendaDto> recebimentosVendas
) {
    public record FuncionarioResumoDto(Long id, String nome) {}
    public record CaixaResumoRecebimentoDto(
            String formaRecebimento,
            BigDecimal vendas,
            BigDecimal suprimentos,
            BigDecimal sangrias,
            BigDecimal despesas,
            BigDecimal resultado
    ) {}
    public record CaixaMovimentacaoDto(
            Instant dataHora,
            TipoMovimentacao tipo,
            String descricao,
            String conta,
            String usuario,
            String formaRecebimento,
            BigDecimal valor
    ) {}
    public record CaixaRecebimentoVendaDto(
            Long vendaId,
            Instant dataVenda,
            Instant ultimaBaixa,
            Long clienteId,
            String clienteNome,
            BigDecimal valorPagoTotal,
            List<FormaPagamentoDto> formasPagamento,
            VendaStatus vendaStatus
    ) {
        public record FormaPagamentoDto(
                String formaRecebimento,
                BigDecimal valorPago,
                int parcelas
        ) {}
    }
}