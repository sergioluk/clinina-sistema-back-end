package com.clinina.sistema.controller;

import com.clinina.sistema.dto.grupo.request.GrupoCategoriaCreateRequestDto;
import com.clinina.sistema.dto.grupo.response.GrupoCategoriaResponseDto;
import com.clinina.sistema.dto.grupo.request.GrupoCreateRequestDto;
import com.clinina.sistema.dto.grupo.response.GrupoResponseDto;
import com.clinina.sistema.service.GrupoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<GrupoResponseDto> criarGrupo(@RequestBody GrupoCreateRequestDto dto) {
        GrupoResponseDto response = grupoService.criarGrupo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/grupo-categorias")
    public void criarGrupoCategoria(@RequestBody GrupoCategoriaCreateRequestDto dto) {
        grupoService.criarGrupoCategoria(dto);
    }

    @GetMapping("/grupo-categorias")
    public List<GrupoCategoriaResponseDto> listarGruposCategorias() {
        return grupoService.listarGruposCategorias();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarGrupo(@PathVariable Long id) {
        grupoService.apagarGrupo(id);
        return ResponseEntity.noContent().build();
    }
}
