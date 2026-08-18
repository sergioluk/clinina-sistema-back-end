package com.clinina.sistema.controller;

import com.clinina.sistema.dto.caixa.request.CaixaDespesaCreateRequestDto;
import com.clinina.sistema.dto.caixa.request.CaixaMovimentacaoCreateRequestDto;
import com.clinina.sistema.dto.caixa.request.CaixaSangriaCreateRequestDto;
import com.clinina.sistema.dto.caixa.request.CaixaTransferenciaCreateRequestDto;
import com.clinina.sistema.service.CaixaMovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/caixas/movimentacoes")
public class CaixaMovimentacaoController {

    private final CaixaMovimentacaoService service;

    public CaixaMovimentacaoController(CaixaMovimentacaoService service) {
        this.service = service;
    }

    @PostMapping("/suprimento")
    public ResponseEntity<Void> criarSuprimento(@RequestBody @Valid CaixaMovimentacaoCreateRequestDto dto) {
        service.salvarSuprimento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sangria")
    public ResponseEntity<Void> criarSangria(@RequestBody @Valid CaixaSangriaCreateRequestDto dto) {
        service.salvarSangria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/despesa")
    public ResponseEntity<Void> criarDespesa(@RequestBody @Valid CaixaDespesaCreateRequestDto dto) {
        service.salvarDespesa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Void> criarTransferencia(@RequestBody @Valid CaixaTransferenciaCreateRequestDto dto) {
        service.salvarTransferencia(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
