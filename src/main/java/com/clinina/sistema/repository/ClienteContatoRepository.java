package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.ClienteContato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteContatoRepository extends JpaRepository<ClienteContato, Long> {
    List<ClienteContato> findByClienteId(Long clienteId);
}
