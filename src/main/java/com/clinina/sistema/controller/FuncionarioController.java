package com.clinina.sistema.controller;

import com.clinina.sistema.dto.funcionario.request.FuncionarioCreateRequestDto;
import com.clinina.sistema.dto.funcionario.response.FuncionarioAtivoResponseDto;
import com.clinina.sistema.dto.funcionario.response.FuncionarioResponseDto;
import com.clinina.sistema.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public List<FuncionarioResponseDto> listar() {
        return funcionarioService.listar();
    }

    @GetMapping("/ativos")
    public List<FuncionarioAtivoResponseDto> listarAtivos() {
        return funcionarioService.listarAtivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(funcionarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponseDto> criar(
            @RequestBody @Valid FuncionarioCreateRequestDto dto
    ) {
        FuncionarioResponseDto response = funcionarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid FuncionarioCreateRequestDto dto
    ) {
        return ResponseEntity.ok(funcionarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        funcionarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
