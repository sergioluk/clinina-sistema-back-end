package com.clinina.sistema.service;

import com.clinina.sistema.model.entity.DespesaCategoria;
import com.clinina.sistema.repository.DespesaCategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespesaCategoriaService {

    private final DespesaCategoriaRepository repository;

    public DespesaCategoriaService(DespesaCategoriaRepository repository) {
        this.repository = repository;
    }

    public List<DespesaCategoria> listarTodas() {
        return repository.findAll();
    }
}
