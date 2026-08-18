package com.clinina.sistema.controller;

import com.clinina.sistema.dto.formaRecebimento.request.FormaRecebimentoCreateRequestDto;
import com.clinina.sistema.dto.formaRecebimento.response.FormaRecebimentoResponseDto;
import com.clinina.sistema.service.FormaRecebimentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formas-recebimento")
public class FormaRecebimentoController {

    private final FormaRecebimentoService service;

    public FormaRecebimentoController(FormaRecebimentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<FormaRecebimentoResponseDto> listarAtivas() {
        return service.listarAtivas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormaRecebimentoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FormaRecebimentoResponseDto> criar(
            @RequestBody @Valid FormaRecebimentoCreateRequestDto dto
    ) {
        FormaRecebimentoResponseDto response = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormaRecebimentoResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid FormaRecebimentoCreateRequestDto dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}