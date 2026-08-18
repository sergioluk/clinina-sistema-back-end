package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository  extends JpaRepository<Animal, Long> {
    List<Animal> findByClienteId(Long clienteId);

    List<Animal> findTop20ByNomeNormalizadoContaining(String termo);
}
