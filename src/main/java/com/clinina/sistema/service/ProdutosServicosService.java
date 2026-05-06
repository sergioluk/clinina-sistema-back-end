package com.clinina.sistema.service;

import com.clinina.sistema.dto.ProdutoCreateRequestDto;
import com.clinina.sistema.dto.ProdutoResponseDto;
import com.clinina.sistema.mapper.ProdutoMapper;
import com.clinina.sistema.model.entity.Grupo;
import com.clinina.sistema.model.entity.Marca;
import com.clinina.sistema.model.entity.Produto;
import com.clinina.sistema.repository.GrupoRepository;
import com.clinina.sistema.repository.MarcaRepository;
import com.clinina.sistema.repository.ProdutosServicosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutosServicosService {

    private final ProdutosServicosRepository produtosServicosRepository;
    private final GrupoRepository grupoRepository;
    private final MarcaRepository marcaRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutosServicosService(ProdutosServicosRepository produtosServicosRepository, GrupoRepository grupoRepository, MarcaRepository marcaRepository, ProdutoMapper produtoMapper) {
        this.produtosServicosRepository = produtosServicosRepository;
        this.grupoRepository = grupoRepository;
        this.marcaRepository = marcaRepository;
        this.produtoMapper = produtoMapper;
    }

    public List<ProdutoResponseDto> listarTodosProdutos() {
        return produtosServicosRepository.findAll()
                .stream()
                .map(produtoMapper::toDto)
                .toList();
    }

    public ProdutoCreateRequestDto criarProduto(ProdutoCreateRequestDto dto) {

        Marca marca = null;
        Grupo grupo = null;

        Produto produto = this.produtoMapper.toEntity(dto);

        if (dto.marcaId() != null) {
            marca = marcaRepository.findById(dto.marcaId())
                    .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
            produto.setMarca(marca);
        }

        if (dto.grupoId() != null) {
            grupo = grupoRepository.findById(dto.grupoId())
                    .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
            produto.setGrupo(grupo);
        }

        Produto salvo = this.produtosServicosRepository.save(produto);

        return this.produtoMapper.toDtoCompleto(salvo);
    }


    public ProdutoCreateRequestDto atualizarProduto(ProdutoCreateRequestDto dto) {
        Produto produto = this.produtosServicosRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtoMapper.updateProdutoFromDto(dto, produto);

        Marca marca = null;
        Grupo grupo = null;

        if (dto.marcaId() != null) {
            marca = marcaRepository.findById(dto.marcaId())
                    .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
            produto.setMarca(marca);
        }

        if (dto.grupoId() != null) {
            grupo = grupoRepository.findById(dto.grupoId())
                    .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
            produto.setGrupo(grupo);
        }

        Produto salvo = produtosServicosRepository.save(produto);
        return this.produtoMapper.toDtoCompleto(salvo);
    }

    public ProdutoCreateRequestDto buscarProdutoPorId(Long id) {
        Produto produto = this.produtosServicosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return this.produtoMapper.toDtoCompleto(produto);
    }
}
