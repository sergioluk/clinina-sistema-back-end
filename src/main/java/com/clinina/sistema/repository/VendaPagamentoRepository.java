package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.VendaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface VendaPagamentoRepository extends JpaRepository<VendaPagamento, Long> {

    @Query("""
        SELECT vp 
        FROM VendaPagamento vp
        JOIN vp.venda v
        WHERE v.caixa.id = :caixaId
        ORDER BY vp.dataPagamento ASC
    """)
    List<VendaPagamento> findPagamentosPorCaixa(@Param("caixaId") Long caixaId);

    @Query("""
        SELECT COALESCE(SUM(vp.valor), 0)
        FROM VendaPagamento vp
        JOIN vp.venda v
        JOIN vp.formaRecebimento fr
        WHERE v.caixa.id = :caixaId
        AND v.status IN (
            com.clinina.sistema.model.enums.VendaStatus.PARCIAL,
            com.clinina.sistema.model.enums.VendaStatus.FINALIZADA
        )
        AND fr.tipo = com.clinina.sistema.model.enums.TipoFormaRecebimento.DINHEIRO
    """)
    BigDecimal somarDinheiroVendasPorCaixa(
            @Param("caixaId") Long caixaId
    );
}