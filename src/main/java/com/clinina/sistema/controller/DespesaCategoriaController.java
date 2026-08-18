package com.clinina.sistema.controller;

import com.clinina.sistema.model.entity.DespesaCategoria;
import com.clinina.sistema.service.DespesaCategoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/despesa-categorias")
public class DespesaCategoriaController {

    private final DespesaCategoriaService service;

    public DespesaCategoriaController(DespesaCategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<DespesaCategoria> listarTodas() {
        return service.listarTodas();
    }
}
