package com.clinina.sistema.service;

import com.clinina.sistema.dto.MarcaCreateRequestDto;
import com.clinina.sistema.dto.MarcaResponseDto;
import com.clinina.sistema.mapper.MarcaMapper;
import com.clinina.sistema.model.entity.Marca;
import com.clinina.sistema.repository.MarcaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public MarcaService(MarcaRepository marcaRepository, MarcaMapper marcaMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    public void criarMarca(MarcaCreateRequestDto dto) {
        Marca marca = this.marcaMapper.toEntity(dto);
        this.marcaRepository.save(marca);
    }

    public List<MarcaResponseDto> listarMarcas() {
        return this.marcaRepository.findAll().stream()
                .map(marcaMapper::toDto).toList();
    }
}
