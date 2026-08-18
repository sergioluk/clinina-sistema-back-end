package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.ClienteMarcacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteMarcacaoRepository extends JpaRepository<ClienteMarcacao, Long> {
    List<ClienteMarcacao> findByClienteId(Long clienteId);
}
