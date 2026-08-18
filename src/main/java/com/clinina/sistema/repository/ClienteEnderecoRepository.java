package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.ClienteEndereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteEnderecoRepository extends JpaRepository<ClienteEndereco, Long> {
    Optional<ClienteEndereco> findByClienteId(Long clienteId);
}
