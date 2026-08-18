package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.CaixaMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CaixaMovimentacaoRepository extends JpaRepository<CaixaMovimentacao, Long> {
    List<CaixaMovimentacao> findByCaixaId(Long caixaId);

    @Query("""
        SELECT COALESCE(SUM(
            CASE
                WHEN cm.tipo = com.clinina.sistema.model.enums.TipoMovimentacao.SUPRIMENTO
                    THEN cm.valor
    
                WHEN cm.tipo IN (
                    com.clinina.sistema.model.enums.TipoMovimentacao.SANGRIA,
                    com.clinina.sistema.model.enums.TipoMovimentacao.DESPESA,
                    com.clinina.sistema.model.enums.TipoMovimentacao.TRANSFERENCIA
                )
                    THEN -cm.valor
    
                ELSE 0
            END
        ),0)
    
        FROM CaixaMovimentacao cm
    
        JOIN cm.formaRecebimento fr
    
        WHERE cm.caixa.id = :caixaId
    
        AND fr.tipo = com.clinina.sistema.model.enums.TipoFormaRecebimento.DINHEIRO
    
    """)
    BigDecimal calcularMovimentacoesDinheiroPorCaixa(
            @Param("caixaId") Long caixaId
    );
}