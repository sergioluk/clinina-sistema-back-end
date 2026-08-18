package com.clinina.sistema.controller;

import com.clinina.sistema.dto.marca.request.MarcaCreateRequestDto;
import com.clinina.sistema.dto.marca.response.MarcaResponseDto;
import com.clinina.sistema.service.MarcaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MarcaResponseDto> criarMarca(@RequestBody MarcaCreateRequestDto dto) {
        MarcaResponseDto response = marcaService.criarMarca(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
