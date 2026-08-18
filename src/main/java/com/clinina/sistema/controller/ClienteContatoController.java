package com.clinina.sistema.controller;

import com.clinina.sistema.dto.cliente.request.ClienteContatoCreateRequestDto;
import com.clinina.sistema.dto.cliente.request.ClienteEditarContatosRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteContatoResponseDto;
import com.clinina.sistema.service.ClienteContatoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes/{clienteId}/contatos")
public class ClienteContatoController {

    private final ClienteContatoService service;

    public ClienteContatoController(ClienteContatoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClienteContatoResponseDto> listar(@PathVariable Long clienteId) {
        return service.listar(clienteId);
    }

    @PostMapping
    public ResponseEntity<ClienteContatoResponseDto> criar(
            @PathVariable Long clienteId,
            @RequestBody @Valid ClienteContatoCreateRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(clienteId, dto));
    }

    @PutMapping("/{contatoId}")
    public ResponseEntity<ClienteContatoResponseDto> atualizar(
            @PathVariable Long clienteId,
            @PathVariable Long contatoId,
            @RequestBody @Valid ClienteContatoCreateRequestDto dto
    ) {
        return ResponseEntity.ok(service.atualizar(clienteId, contatoId, dto));
    }

    @DeleteMapping("/{contatoId}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long clienteId,
            @PathVariable Long contatoId
    ) {
        service.deletar(clienteId, contatoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<ClienteEditarContatosRequestDto> atualizarContatos(
            @PathVariable Long clienteId,
            @RequestBody @Valid ClienteEditarContatosRequestDto dto
    ) {
        ClienteEditarContatosRequestDto response = service.atualizarContatos(clienteId, dto);
        return ResponseEntity.ok(response);
    }


}
