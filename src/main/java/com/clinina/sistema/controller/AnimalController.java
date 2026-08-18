package com.clinina.sistema.controller;

import com.clinina.sistema.dto.animal.response.AnimalCompletoResponseDto;
import com.clinina.sistema.dto.animal.request.AnimalCreateRequestDto;
import com.clinina.sistema.dto.animal.response.AnimalPesquisarClienteResponseDto;
import com.clinina.sistema.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<AnimalCompletoResponseDto> listar() {
        return animalService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalCompletoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AnimalCompletoResponseDto> criar(
            @RequestBody @Valid AnimalCreateRequestDto dto
    ) {
        AnimalCompletoResponseDto response = animalService.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalCompletoResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AnimalCreateRequestDto dto
    ) {
        return ResponseEntity.ok(animalService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        animalService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AnimalPesquisarClienteResponseDto>> buscarAnimaisPorNome(
            @RequestParam String termo
    ) {
        return ResponseEntity.ok(animalService.buscarAnimaisPorNome(termo));
    }
}