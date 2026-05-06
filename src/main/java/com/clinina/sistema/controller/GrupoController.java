package com.clinina.sistema.controller;

import com.clinina.sistema.dto.GrupoCategoriaCreateRequestDto;
import com.clinina.sistema.dto.GrupoCategoriaResponseDto;
import com.clinina.sistema.dto.GrupoCreateRequestDto;
import com.clinina.sistema.dto.GrupoResponseDto;
import com.clinina.sistema.model.entity.Grupo;
import com.clinina.sistema.service.GrupoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public List<GrupoResponseDto> listarGrupos() {
        return grupoService.listarGrupos();
    }

    @PostMapping
    public void criarGrupo(@RequestBody GrupoCreateRequestDto dto) {
        grupoService.criarGrupo(dto);
    }

    @PostMapping("/grupo-categorias")
    public void criarGrupoCategoria(@RequestBody GrupoCategoriaCreateRequestDto dto) {
        grupoService.criarGrupoCategoria(dto);
    }

    @GetMapping("/grupo-categorias")
    public List<GrupoCategoriaResponseDto> listarGruposCategorias() {
        return grupoService.listarGruposCategorias();
    }
}
