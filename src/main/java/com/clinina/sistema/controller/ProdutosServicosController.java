package com.clinina.sistema.controller;

import com.clinina.sistema.dto.produto.request.*;
import com.clinina.sistema.dto.produto.response.*;
import com.clinina.sistema.service.ProdutosServicosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos-servicos")
public class ProdutosServicosController {

    private final ProdutosServicosService produtosServicosService;

    public ProdutosServicosController(ProdutosServicosService produtosServicosService) {
        this.produtosServicosService = produtosServicosService;
    }

    @GetMapping
    public ProdutoPageResponseDto listarProdutosServicos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho,
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String ordenarPor,
            @RequestParam(required = false) String direcao
    ) {
        return produtosServicosService.listarProdutos(pagina, tamanho, busca, ordenarPor, direcao);
    }

    @GetMapping("/validade")
    public ValidadeProdutosResponseDto listarProdutosPorValidade() {
        return produtosServicosService.buscarProdutosPorValidade();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDetalhesResponseDto> buscarProdutoServicoPorId(@PathVariable Long id) {
        ProdutoDetalhesResponseDto produto = this.produtosServicosService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<ProdutoCreateRequestDto> criarProduto(@RequestBody ProdutoCreateRequestDto dto) {
        ProdutoCreateRequestDto response = this.produtosServicosService.criarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoCreateRequestDto> atualizarProduto(@PathVariable Long id, @RequestBody @Valid ProdutoCreateRequestDto dto) {
        ProdutoCreateRequestDto response = this.produtosServicosService.atualizarProduto(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/existe-codigo-barras")
    public ResponseEntity<Boolean> verificarCodigoDeBarrasExiste(@RequestParam String codigoDeBarras) {
        boolean existe = produtosServicosService.existeCodigoDeBarras(codigoDeBarras);
        return ResponseEntity.ok(existe);
    }

    @PatchMapping("/{produtoId}/dados-basicos")
    public ResponseEntity<Void> atualizarDadosBasicos(
            @PathVariable Long produtoId,
            @RequestBody ProdutoDadosBasicosUpdateRequestDto dto
    ) {
        this.produtosServicosService.atualizarDadosBasicos(produtoId, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{produtoId}/custo-preco")
    public ResponseEntity<Void> atualizarCustoPreco(
            @PathVariable Long produtoId,
            @RequestBody ProdutoCustoPrecoUpdateRequestDto dto
    ) {
        this.produtosServicosService.atualizarCustoPreco(produtoId, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{produtoId}/validade")
    public ResponseEntity<Void> atualizarValidade(
            @PathVariable Long produtoId,
            @RequestBody ProdutoValidadeUpdateRequestDto dto
    ) {
        this.produtosServicosService.atualizarValidade(produtoId, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{produtoId}/limite-desconto")
    public ResponseEntity<Void> atualizarLimiteDesconto(
            @PathVariable Long produtoId,
            @RequestBody ProdutoLimiteDescontoUpdateDto dto
    ) {
        this.produtosServicosService.atualizarLimiteDesconto(produtoId, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{produtoId}/estoque")
    public ResponseEntity<Void> atualizarEstoque(
            @PathVariable Long produtoId,
            @RequestBody ProdutoEstoqueUpdateRequestDto dto
    ) {
        this.produtosServicosService.atualizarEstoque(produtoId, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoBuscaResponseDto>> buscarProdutos(@RequestParam String termo) {
        List<ProdutoBuscaResponseDto> produtos = produtosServicosService.buscarProdutos(termo);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/codigo-barras/{codigoDeBarras}")
    public ResponseEntity<ProdutoBuscaResponseDto> buscarProdutoServicoPorCodigoBarras(@PathVariable String codigoDeBarras) {
        ProdutoBuscaResponseDto produto = produtosServicosService.buscarPorCodigoDeBarras(codigoDeBarras);
        return ResponseEntity.ok(produto);
    }

}
