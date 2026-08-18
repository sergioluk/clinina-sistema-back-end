package com.clinina.sistema.service;

import com.clinina.sistema.dto.cliente.request.ClienteEditarEnderecoRequestDto;
import com.clinina.sistema.dto.cliente.request.ClienteEnderecoCreateRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteEnderecoResponseDto;
import com.clinina.sistema.mapper.ClienteEnderecoMapper;
import com.clinina.sistema.model.entity.Cliente;
import com.clinina.sistema.model.entity.ClienteEndereco;
import com.clinina.sistema.repository.ClienteEnderecoRepository;
import com.clinina.sistema.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClienteEnderecoService {

    private final ClienteRepository clienteRepository;
    private final ClienteEnderecoRepository enderecoRepository;
    private final ClienteEnderecoMapper mapper;

    public ClienteEnderecoService(
            ClienteRepository clienteRepository,
            ClienteEnderecoRepository enderecoRepository,
            ClienteEnderecoMapper mapper
    ) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.mapper = mapper;
    }

    public ClienteEnderecoResponseDto buscarPorCliente(Long clienteId) {
        ClienteEndereco endereco = enderecoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

        return mapper.toResponseDto(endereco);
    }

    @Transactional
    public ClienteEnderecoResponseDto criar(Long clienteId, ClienteEnderecoCreateRequestDto dto) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        if (cliente.getEndereco() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente já possui endereço cadastrado");
        }

        ClienteEndereco endereco = mapper.toEntity(dto);
        endereco.setCliente(cliente);
        cliente.setEndereco(endereco);

        ClienteEndereco salvo = enderecoRepository.save(endereco);
        return mapper.toResponseDto(salvo);
    }

    @Transactional
    public ClienteEnderecoResponseDto atualizar(Long clienteId, Long enderecoId, ClienteEnderecoCreateRequestDto dto) {
        ClienteEndereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

        if (!endereco.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endereço não pertence a este cliente");
        }

        mapper.updateFromDto(dto, endereco);

        return mapper.toResponseDto(endereco);
    }

    @Transactional
    public void deletar(Long clienteId, Long enderecoId) {
        ClienteEndereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

        if (!endereco.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endereço não pertence a este cliente");
        }

        enderecoRepository.delete(endereco);
    }

    @Transactional
    public ClienteEditarEnderecoRequestDto atualizarEndereco(Long clienteId, ClienteEditarEnderecoRequestDto dto) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        ClienteEndereco endereco = cliente.getEndereco();
        if (endereco == null) {
            endereco = new ClienteEndereco();
            endereco.setCliente(cliente);
            cliente.setEndereco(endereco);
        }

        endereco.setCep(dto.cep());
        endereco.setRua(dto.rua());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setPontoReferencia(dto.pontoReferencia());

        clienteRepository.save(cliente);

        return dto;
    }
}
