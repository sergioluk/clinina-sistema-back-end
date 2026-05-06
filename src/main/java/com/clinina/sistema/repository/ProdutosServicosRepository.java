package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosServicosRepository extends JpaRepository<Produto, Long> {
}
