package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findTop20ByNomeNormalizadoContaining(String termo);
}
