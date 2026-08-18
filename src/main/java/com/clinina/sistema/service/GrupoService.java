package com.clinina.sistema.service;

import com.clinina.sistema.dto.grupo.request.GrupoCategoriaCreateRequestDto;
import com.clinina.sistema.dto.grupo.response.GrupoCategoriaResponseDto;
import com.clinina.sistema.dto.grupo.request.GrupoCreateRequestDto;
import com.clinina.sistema.dto.grupo.response.GrupoResponseDto;
import com.clinina.sistema.mapper.GrupoMapper;
import com.clinina.sistema.model.entity.Grupo;
import com.clinina.sistema.model.entity.GrupoCategoria;
import com.clinina.sistema.repository.GrupoCategoriaRepository;
import com.clinina.sistema.repository.GrupoRepository;
import com.clinina.sistema.repository.ProdutosServicosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final GrupoCategoriaRepository grupoCategoriaRepository;
    private final ProdutosServicosRepository produtoRepository;
    private final GrupoMapper grupoMapper;

    public GrupoService(GrupoRepository grupoRepository, GrupoCategoriaRepository grupoCategoriaRepository, ProdutosServicosRepository produtoRepository, GrupoMapper grupoMapper) {
        this.grupoRepository = grupoRepository;
        this.grupoCategoriaRepository = grupoCategoriaRepository;
        this.produtoRepository = produtoRepository;
        this.grupoMapper = grupoMapper;
    }

    public GrupoResponseDto criarGrupo(GrupoCreateRequestDto dto) {
        GrupoCategoria grupoCategoria = this.grupoCategoriaRepository.findById(dto.grupoCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Grupo grupo = this.grupoMapper.toEntity(dto);
        grupo.setGrupoCategoria(grupoCategoria);

        Grupo grupoSalvo = this.grupoRepository.save(grupo);
        return this.grupoMapper.toDto(grupoSalvo);
    }

    public List<GrupoResponseDto> listarGrupos() {
        return this.grupoRepository.findAll().stream()
                .map(grupoMapper::toDto).toList();
    }

    public void criarGrupoCategoria(GrupoCategoriaCreateRequestDto dto) {
        GrupoCategoria grupoCategoria = this.grupoMapper.toGrupoCategoria(dto);
        this.grupoCategoriaRepository.save(grupoCategoria);
    }

    public List<GrupoCategoriaResponseDto> listarGruposCategorias() {
        return this.grupoCategoriaRepository.findAll().stream()
                .map(this.grupoMapper::toGrupoCategoriaDto).toList();
    }

    public void apagarGrupo(Long id) {
        Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo não encontrado"));

        boolean possuiProdutos = produtoRepository.existsByGrupo(grupo);
        if (possuiProdutos) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível apagar um grupo que possui produtos");
        }

        grupoRepository.delete(grupo);
    }

}
