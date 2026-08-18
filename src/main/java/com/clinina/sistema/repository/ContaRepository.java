package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByNome(String nome);
}
