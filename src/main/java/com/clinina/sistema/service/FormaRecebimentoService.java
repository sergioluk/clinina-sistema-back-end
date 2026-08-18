package com.clinina.sistema.service;

import com.clinina.sistema.dto.formaRecebimento.request.FormaRecebimentoCreateRequestDto;
import com.clinina.sistema.dto.formaRecebimento.response.FormaRecebimentoResponseDto;
import com.clinina.sistema.mapper.FormaRecebimentoMapper;
import com.clinina.sistema.model.entity.FormaRecebimento;
import com.clinina.sistema.repository.FormaRecebimentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FormaRecebimentoService {

    private final FormaRecebimentoRepository repository;
    private final FormaRecebimentoMapper mapper;

    public FormaRecebimentoService(
            FormaRecebimentoRepository repository,
            FormaRecebimentoMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<FormaRecebimentoResponseDto> listarAtivas() {
        return repository.findByAtivoTrueOrderByNomeAsc()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    public FormaRecebimentoResponseDto buscarPorId(Long id) {
        FormaRecebimento forma = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Forma de recebimento não encontrada"
                ));

        return mapper.toResponseDto(forma);
    }

    @Transactional
    public FormaRecebimentoResponseDto criar(FormaRecebimentoCreateRequestDto dto) {
        FormaRecebimento forma = mapper.toEntity(dto);
        forma.setAtivo(true);

        FormaRecebimento salvo = repository.save(forma);
        return mapper.toResponseDto(salvo);
    }

    @Transactional
    public FormaRecebimentoResponseDto atualizar(Long id, FormaRecebimentoCreateRequestDto dto) {
        FormaRecebimento forma = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Forma de recebimento não encontrada"
                ));

        mapper.updateFromDto(dto, forma);

        return mapper.toResponseDto(forma);
    }

    @Transactional
    public void desativar(Long id) {
        FormaRecebimento forma = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Forma de recebimento não encontrada"
                ));

        forma.setAtivo(false);
    }
}
