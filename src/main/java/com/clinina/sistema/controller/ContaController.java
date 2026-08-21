package com.clinina.sistema.controller;

import com.clinina.sistema.dto.conta.request.ContaCreateRequestDto;
import com.clinina.sistema.dto.conta.response.ContaResponseDto;
import com.clinina.sistema.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping
    public List<ContaResponseDto> listar() {
        return contaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ContaResponseDto> criar(@RequestBody @Valid ContaCreateRequestDto dto) {
        ContaResponseDto salvo = contaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaResponseDto> atualizar(@PathVariable Long id,
                                                      @RequestBody @Valid ContaCreateRequestDto dto) {
        ContaResponseDto atualizado = contaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> getSaldoPorCaixaFuncionario(
            @RequestParam Long caixaId,
            @RequestParam Long funcionarioId
    ) {
        BigDecimal saldo = contaService.getSaldoContaPorCaixaFuncionario(caixaId, funcionarioId);
        return ResponseEntity.ok(saldo);
    }

    @GetMapping("/ping")
    public Map<String, String> pingar() {
        System.out.println("Pingou");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pingou");
        return response;
    }
}
