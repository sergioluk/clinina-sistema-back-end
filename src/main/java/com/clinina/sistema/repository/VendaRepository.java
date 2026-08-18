package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.Venda;
import com.clinina.sistema.model.enums.VendaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    @Query("""
        SELECT COALESCE(SUM(v.valorTotal), 0)
        FROM Venda v
        WHERE v.cliente.id = :clienteId
        AND v.status IN (com.clinina.sistema.model.enums.VendaStatus.ABERTA, com.clinina.sistema.model.enums.VendaStatus.EM_ATENDIMENTO)
    """)
    BigDecimal somarVendasEmAbertoEEmAtendimentoPorCliente(@Param("clienteId") Long clienteId);

    List<Venda> findByCaixaId(Long caixaId);

    List<Venda> findByClienteIdAndStatus(Long clienteId, VendaStatus status);
    List<Venda> findByClienteIdAndStatusIn(Long clienteId, List<VendaStatus> status);

    List<Venda> findByClienteId(Long clienteId);

    Optional<Venda> findFirstByClienteIdAndStatus(Long clienteId, VendaStatus status);

    @Query("""
    SELECT COALESCE(SUM(v.valorTotal), 0)
    FROM Venda v
    WHERE v.cliente.id = :clienteId
    AND v.status IN (
        com.clinina.sistema.model.enums.VendaStatus.ABERTA,
        com.clinina.sistema.model.enums.VendaStatus.EM_ATENDIMENTO,
        com.clinina.sistema.model.enums.VendaStatus.PARCIAL
    )
""")
    BigDecimal somarVendasAbertasEmAtendimentoEParcialPorCliente(@Param("clienteId") Long clienteId);

    Optional<Venda> findFirstByCaixaIdOrderByDataHoraVendaDesc(Long caixaId);

    List<Venda> findByDataHoraVendaBetweenOrderByDataHoraVendaAsc(Instant dataInicio, Instant dataFim);

    @Query("""
        SELECT v FROM Venda v
        WHERE (:clienteId IS NULL OR v.cliente.id = :clienteId)
        AND (:status IS NULL OR v.status = :status)
        AND (:inicio IS NULL OR v.dataHoraVenda >= :inicio)
        AND (:fim IS NULL OR v.dataHoraVenda <= :fim)
    """)
    List<Venda> filtrarVendas(
            @Param("clienteId") Long clienteId,
            @Param("status") VendaStatus status,
            @Param("inicio") Instant inicio,
            @Param("fim") Instant fim
    );
}
