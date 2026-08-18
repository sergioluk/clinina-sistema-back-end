package com.clinina.sistema.service;

import com.clinina.sistema.dto.cliente.request.ClienteMarcacaoCreateRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteMarcacaoResponseDto;
import com.clinina.sistema.mapper.ClienteMarcacaoMapper;
import com.clinina.sistema.model.entity.Cliente;
import com.clinina.sistema.model.entity.ClienteMarcacao;
import com.clinina.sistema.repository.ClienteMarcacaoRepository;
import com.clinina.sistema.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClienteMarcacaoService {

    private final ClienteRepository clienteRepository;
    private final ClienteMarcacaoRepository marcacaoRepository;
    private final ClienteMarcacaoMapper mapper;

    public ClienteMarcacaoService(
            ClienteRepository clienteRepository,
            ClienteMarcacaoRepository marcacaoRepository,
            ClienteMarcacaoMapper mapper
    ) {
        this.clienteRepository = clienteRepository;
        this.marcacaoRepository = marcacaoRepository;
        this.mapper = mapper;
    }

    public List<ClienteMarcacaoResponseDto> listar(Long clienteId) {
        return marcacaoRepository.findByClienteId(clienteId)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Transactional
    public ClienteMarcacaoResponseDto criar(Long clienteId, ClienteMarcacaoCreateRequestDto dto) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        ClienteMarcacao marcacao = mapper.toEntity(dto);
        marcacao.setCliente(cliente);

        ClienteMarcacao salvo = marcacaoRepository.save(marcacao);
        return mapper.toResponseDto(salvo);
    }

    @Transactional
    public ClienteMarcacaoResponseDto atualizar(Long clienteId, Long marcacaoId, ClienteMarcacaoCreateRequestDto dto) {
        ClienteMarcacao marcacao = marcacaoRepository.findById(marcacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marcação não encontrada"));

        if (!marcacao.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Marcação não pertence a este cliente");
        }

        mapper.updateFromDto(dto, marcacao);

        return mapper.toResponseDto(marcacao);
    }

    @Transactional
    public void deletar(Long clienteId, Long marcacaoId) {
        ClienteMarcacao marcacao = marcacaoRepository.findById(marcacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marcação não encontrada"));

        if (!marcacao.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Marcação não pertence a este cliente");
        }

        marcacaoRepository.delete(marcacao);
    }
}
