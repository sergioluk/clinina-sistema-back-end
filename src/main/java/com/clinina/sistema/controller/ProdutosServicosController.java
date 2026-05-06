package com.clinina.sistema.controller;

import com.clinina.sistema.dto.ProdutoCreateRequestDto;
import com.clinina.sistema.dto.ProdutoResponseDto;
import com.clinina.sistema.model.entity.Produto;
import com.clinina.sistema.service.ProdutosServicosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public List<ProdutoResponseDto> listarProdutosServicos() {
        return this.produtosServicosService.listarTodosProdutos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoCreateRequestDto> buscarProdutoServicoPorId(@PathVariable Long id) {
        ProdutoCreateRequestDto produto = this.produtosServicosService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<ProdutoCreateRequestDto> criarProduto(@RequestBody ProdutoCreateRequestDto dto) {
        ProdutoCreateRequestDto response = this.produtosServicosService.criarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<ProdutoCreateRequestDto> atualizarProduto(@RequestBody ProdutoCreateRequestDto dto) {
        ProdutoCreateRequestDto response = this.produtosServicosService.atualizarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
