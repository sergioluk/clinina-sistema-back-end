package com.clinina.sistema.service;

import com.clinina.sistema.dto.GrupoCategoriaCreateRequestDto;
import com.clinina.sistema.dto.GrupoCategoriaResponseDto;
import com.clinina.sistema.dto.GrupoCreateRequestDto;
import com.clinina.sistema.dto.GrupoResponseDto;
import com.clinina.sistema.mapper.GrupoMapper;
import com.clinina.sistema.model.entity.Grupo;
import com.clinina.sistema.model.entity.GrupoCategoria;
import com.clinina.sistema.repository.GrupoCategoriaRepository;
import com.clinina.sistema.repository.GrupoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final GrupoCategoriaRepository grupoCategoriaRepository;
    private final GrupoMapper grupoMapper;

    public GrupoService(GrupoRepository grupoRepository, GrupoCategoriaRepository grupoCategoriaRepository, GrupoMapper grupoMapper) {
        this.grupoRepository = grupoRepository;
        this.grupoCategoriaRepository = grupoCategoriaRepository;
        this.grupoMapper = grupoMapper;
    }

    public void criarGrupo(GrupoCreateRequestDto dto) {
        GrupoCategoria grupoCategoria = this.grupoCategoriaRepository.findById(dto.grupoCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Grupo grupo = this.grupoMapper.toEntity(dto);
        grupo.setGrupoCategoria(grupoCategoria);

        this.grupoRepository.save(grupo);
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
}
