package com.clinina.sistema.controller;

import com.clinina.sistema.dto.caixa.request.CaixaCreateRequestDto;
import com.clinina.sistema.dto.caixa.request.CaixaEncerrarRequestDto;
import com.clinina.sistema.dto.caixa.response.CaixaAtualUsuarioResponseDto;
import com.clinina.sistema.dto.caixa.response.CaixaDetalheResponseDto;
import com.clinina.sistema.dto.caixa.response.CaixaListaDiaDto;
import com.clinina.sistema.dto.caixa.response.CaixaResponseDto;
import com.clinina.sistema.model.enums.StatusCaixa;
import com.clinina.sistema.service.CaixaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/caixas")
public class CaixaController {

    private final CaixaService caixaService;

    public CaixaController(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @GetMapping
    public List<CaixaResponseDto> listar() {
        return caixaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaixaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(caixaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CaixaResponseDto> criar(@RequestBody @Valid CaixaCreateRequestDto dto) {
        CaixaResponseDto response = caixaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CaixaResponseDto> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusCaixa status
    ) {
        CaixaResponseDto response = caixaService.atualizarStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        caixaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/abertos")
    public List<CaixaResponseDto> listarAtivos() {
        return caixaService.listar()
                .stream()
                .filter(c -> c.status() == StatusCaixa.ABERTO)
                .toList();
    }




    @PostMapping("/abrir")
    public ResponseEntity<CaixaResponseDto> abrirCaixa(@RequestBody CaixaCreateRequestDto dto) {
        CaixaResponseDto response = caixaService.abrirCaixa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/dia")
    public List<CaixaListaDiaDto> listarCaixasPorDia(@RequestParam String data) {
        LocalDate dia = LocalDate.parse(data);
        return caixaService.listarCaixasPorDia(dia);
    }

    @GetMapping("/{id}/detalhes")
    public CaixaDetalheResponseDto detalhesCaixa(@PathVariable Long id) {
        return caixaService.detalhesCaixa(id);
    }

    @PatchMapping("/{id}/fechar")
    public ResponseEntity<CaixaResponseDto> fecharCaixa(@PathVariable Long id) {
        CaixaResponseDto response = caixaService.fecharCaixa(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario")
    public ResponseEntity<CaixaAtualUsuarioResponseDto> buscarCaixaDoUsuario(
            @RequestParam Long usuarioId) {

        CaixaAtualUsuarioResponseDto response =
                caixaService.buscarCaixaAtualDoUsuario(usuarioId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<String> finalizarCaixa(
            @PathVariable Long id,
            @RequestBody CaixaEncerrarRequestDto dto
    ) {
        String mensagem = caixaService.finalizarCaixa(id, dto);
        return ResponseEntity.ok(mensagem);
    }

    @GetMapping("/{caixaId}/saldo")
    public ResponseEntity<BigDecimal> obterSaldo(@PathVariable Long caixaId) {
        BigDecimal saldo = caixaService.obterSaldo(caixaId);
        return ResponseEntity.ok(saldo);
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<CaixaResponseDto> reabrirCaixa(
            @PathVariable Long id
    ) {
        CaixaResponseDto response = caixaService.reabrirCaixa(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/abertos-fechados")
    public List<CaixaResponseDto> listarCaixasAbertosFechados() {
        return caixaService.listarCaixasAbertosFechados();
    }
}
