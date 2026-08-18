package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.Conta;
import com.clinina.sistema.model.entity.FormaRecebimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormaRecebimentoRepository extends JpaRepository<FormaRecebimento, Long> {
    List<FormaRecebimento> findByAtivoTrueOrderByNomeAsc();

    Optional<FormaRecebimento> findByNome(String nome);
}
