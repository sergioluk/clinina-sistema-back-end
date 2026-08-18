package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioRepository  extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByAtivoTrueOrderByNomeAsc();
}
