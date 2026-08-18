package com.clinina.sistema.controller;

import com.clinina.sistema.dto.venda.request.*;
import com.clinina.sistema.dto.venda.response.*;
import com.clinina.sistema.model.enums.VendaStatus;
import com.clinina.sistema.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<VendaResponseDto> criarVenda(
            @RequestBody @Valid VendaCreateRequestDto dto
    ) {
        VendaResponseDto response = vendaService.criarVenda(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/atendimento")
    public ResponseEntity<VendaResponseDto> criarVendaEmAtendimento(
            @RequestBody @Valid VendaCreateOpenRequestDto dto
    ) {
        VendaResponseDto response = vendaService.criarVendaEmAtendimento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/finalizar")
    public ResponseEntity<List<VendaResponseDto>> finalizarVenda(@RequestBody @Valid VendaFinalizeCreateRequestDto dto) {
        List<VendaResponseDto> response = vendaService.finalizarVendas(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @GetMapping
    public List<VendaResponseDto> listar() {
        return vendaService.listar();
    }


    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<VendaResponseDto> cancelarVenda(@PathVariable Long id, @RequestBody @Valid VendaCancelarRequestDto dto) {

        VendaResponseDto response = vendaService.cancelarVenda(id, dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/caixa/{caixaId}")
    public List<VendaResponseDto> listarVendasPorCaixa(@PathVariable Long caixaId) {
        return vendaService.listarVendasPorCaixa(caixaId);
    }

    @GetMapping("/cliente/{clienteId}/abertas-e-em-atendimento")
    public List<VendaRegistrarRecebimentoResponseDto> listarVendasAbertasEEmAtendimento(
            @PathVariable Long clienteId
    ) {
        return vendaService.listarVendasAbertasEmAtendimentoPorCliente(clienteId);
    }

    @GetMapping("/cliente/{clienteId}/completo-abertas-e-em-atendimento")
    public List<VendaResponseDto> listarVendasCompletoAbertasEEmAtendimento(
            @PathVariable Long clienteId
    ) {
        return vendaService.listarVendasCompletoAbertasEEmAtendimento(clienteId);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<VendaResponseDto> listarVendasPorCliente(
            @PathVariable Long clienteId,
            @RequestParam(required = false) VendaStatus status
    ) {
        return vendaService.listarVendasPorCliente(clienteId, status);
    }

    @GetMapping("/cliente/{clienteId}/em-atendimento")
    public ResponseEntity<VendaResponseDto> buscarVendaEmAtendimento(
            @PathVariable Long clienteId
    ) {
        VendaResponseDto response = vendaService.buscarVendaEmAtendimentoPorCliente(clienteId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/alterar-status")
    public ResponseEntity<VendaResponseDto> alterarStatusVenda(
            @PathVariable Long id,
            @RequestBody AtualizarStatusVendaRequestDto dto
    ) {
        VendaResponseDto response = vendaService.alterarStatusVenda(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/pagamentos/historico")
    public ResponseEntity<List<VendaPagamentoHistoricoResponseDto>> historicoPagamentos(
            @PathVariable Long id
    ) {
        List<VendaPagamentoHistoricoResponseDto> historico = vendaService.buscarHistoricoPagamentos(id);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/ultima/{caixaId}")
    public ResponseEntity<VendaResponseDto> ultimaVendaPorCaixa(@PathVariable Long caixaId) {
        VendaResponseDto venda = vendaService.buscarUltimaVendaPorCaixa(caixaId);
        return ResponseEntity.ok(venda);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<VendaListaPdvResponseDto>> listarVendasPorPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {

        List<VendaListaPdvResponseDto> vendas =
                vendaService.listarVendasPorPeriodo(dataInicio, dataFim);

        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/localizarVendas")
    public ResponseEntity<List<LocalizarVendaResponseDto>> localizarVendas(
            @ModelAttribute LocalizarVendaRequestDto filtro) {
        System.out.println("Filtro recebido: " + filtro);
        List<LocalizarVendaResponseDto> vendas = vendaService.localizarVendas(filtro);

        return ResponseEntity.ok(vendas);
    }

}
