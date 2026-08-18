package com.clinina.sistema.controller;

import com.clinina.sistema.dto.cliente.request.ClienteEditarEnderecoRequestDto;
import com.clinina.sistema.dto.cliente.request.ClienteEnderecoCreateRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteEnderecoResponseDto;
import com.clinina.sistema.service.ClienteEnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes/{clienteId}/endereco")
public class ClienteEnderecoController {

    private final ClienteEnderecoService service;

    public ClienteEnderecoController(ClienteEnderecoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ClienteEnderecoResponseDto> buscar(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.buscarPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<ClienteEnderecoResponseDto> criar(
            @PathVariable Long clienteId,
            @RequestBody @Valid ClienteEnderecoCreateRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(clienteId, dto));
    }

    @PutMapping("/{enderecoId}")
    public ResponseEntity<ClienteEnderecoResponseDto> atualizar(
            @PathVariable Long clienteId,
            @PathVariable Long enderecoId,
            @RequestBody @Valid ClienteEnderecoCreateRequestDto dto
    ) {
        return ResponseEntity.ok(service.atualizar(clienteId, enderecoId, dto));
    }

    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long clienteId,
            @PathVariable Long enderecoId
    ) {
        service.deletar(clienteId, enderecoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<ClienteEditarEnderecoRequestDto> atualizarEndereco(
            @PathVariable Long clienteId,
            @RequestBody @Valid ClienteEditarEnderecoRequestDto dto
    ) {
        ClienteEditarEnderecoRequestDto response = service.atualizarEndereco(clienteId, dto);
        return ResponseEntity.ok(response);
    }
}
