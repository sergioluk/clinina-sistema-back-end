package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.Caixa;
import com.clinina.sistema.model.enums.StatusCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {

    List<Caixa> findByStatus(StatusCaixa status);

    List<Caixa> findByDataHoraAberturaBetween(Instant inicio, Instant fim);



    @Query("""
    SELECT c
    FROM Caixa c
    WHERE c.funcionario.id = :funcionarioId
    AND c.status <> com.clinina.sistema.model.enums.StatusCaixa.FINALIZADO
    """)
    Optional<Caixa> findCaixaAtualDoFuncionario(Long funcionarioId);

    List<Caixa> findByStatusIn(List<StatusCaixa> status);
}
