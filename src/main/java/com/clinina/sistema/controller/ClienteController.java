package com.clinina.sistema.controller;

import com.clinina.sistema.dto.cliente.request.ClienteCreateRequestDto;
import com.clinina.sistema.dto.cliente.request.ClienteEditarDadosGeraisRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteBuscaResponseDto;
import com.clinina.sistema.dto.cliente.response.ClienteCompletoResponseDto;
import com.clinina.sistema.dto.cliente.response.ClientePdvResponseDto;
import com.clinina.sistema.dto.cliente.response.ClientePerfilResponseDto;
import com.clinina.sistema.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    public List<ClienteBuscaResponseDto> listarClientes() {
        return this.clienteService.listarClientes();
    }

    @PostMapping
    public ResponseEntity<ClienteCompletoResponseDto> criarCliente(@RequestBody @Valid ClienteCreateRequestDto dto) {
        ClienteCompletoResponseDto response = this.clienteService.criarCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteBuscaResponseDto>> buscarClientesPorNome(
            @RequestParam String termo
    ) {
        return ResponseEntity.ok(clienteService.buscarClientesPorNome(termo));
    }

    @GetMapping("/{id}/pdv")
    public ResponseEntity<ClientePdvResponseDto> buscarClientePdvPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(clienteService.buscarClientePdvPorId(id));
    }

    @GetMapping("/{id}/saldo-devedor")
    public ResponseEntity<BigDecimal> buscarSaldoDevedorPorId(@PathVariable Long id) {
            return ResponseEntity.ok(clienteService.buscarSaldoDevedorPorId(id));
    }

    @GetMapping("/{id}/perfil")
    public ResponseEntity<ClientePerfilResponseDto> buscarPerfilCliente(@PathVariable Long id) {
        ClientePerfilResponseDto dto = clienteService.buscarClientePerfilPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/editar-dados-gerais")
    public ResponseEntity<ClienteEditarDadosGeraisRequestDto> atualizarDadosGerais(
            @PathVariable Long id,
            @RequestBody @Valid ClienteEditarDadosGeraisRequestDto dto
    ) {
        ClienteEditarDadosGeraisRequestDto response = clienteService.atualizarDadosGerais(id, dto);
        return ResponseEntity.ok(response);
    }
}
