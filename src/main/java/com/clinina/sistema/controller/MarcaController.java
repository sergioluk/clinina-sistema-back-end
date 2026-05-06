package com.clinina.sistema.controller;

import com.clinina.sistema.dto.MarcaCreateRequestDto;
import com.clinina.sistema.dto.MarcaResponseDto;
import com.clinina.sistema.model.entity.Marca;
import com.clinina.sistema.service.MarcaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public List<MarcaResponseDto> listarMarcas() {
        return marcaService.listarMarcas();
    }

    @PostMapping
    public void criarMarca(@RequestBody MarcaCreateRequestDto dto) {
        marcaService.criarMarca(dto);
    }
}
