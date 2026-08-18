package com.clinina.sistema.controller;

import com.clinina.sistema.dto.cliente.request.ClienteMarcacaoCreateRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteMarcacaoResponseDto;
import com.clinina.sistema.service.ClienteMarcacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes/{clienteId}/marcacoes")
public class ClienteMarcacaoController {

    private final ClienteMarcacaoService service;

    public ClienteMarcacaoController(ClienteMarcacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClienteMarcacaoResponseDto> listar(@PathVariable Long clienteId) {
        return service.listar(clienteId);
    }

    @PostMapping
    public ResponseEntity<ClienteMarcacaoResponseDto> criar(
            @PathVariable Long clienteId,
            @RequestBody @Valid ClienteMarcacaoCreateRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(clienteId, dto));
    }

    @PutMapping("/{marcacaoId}")
    public ResponseEntity<ClienteMarcacaoResponseDto> atualizar(
            @PathVariable Long clienteId,
            @PathVariable Long marcacaoId,
            @RequestBody @Valid ClienteMarcacaoCreateRequestDto dto
    ) {
        return ResponseEntity.ok(service.atualizar(clienteId, marcacaoId, dto));
    }

    @DeleteMapping("/{marcacaoId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long clienteId,
            @PathVariable Long marcacaoId
    ) {
        service.deletar(clienteId, marcacaoId);
        return ResponseEntity.noContent().build();
    }
}
