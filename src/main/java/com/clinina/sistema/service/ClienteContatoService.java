package com.clinina.sistema.service;

import com.clinina.sistema.dto.cliente.request.ClienteContatoCreateRequestDto;
import com.clinina.sistema.dto.cliente.request.ClienteEditarContatosRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteContatoResponseDto;
import com.clinina.sistema.mapper.ClienteContatoMapper;
import com.clinina.sistema.model.entity.Cliente;
import com.clinina.sistema.model.entity.ClienteContato;
import com.clinina.sistema.repository.ClienteContatoRepository;
import com.clinina.sistema.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClienteContatoService {

    private final ClienteRepository clienteRepository;
    private final ClienteContatoRepository contatoRepository;
    private final ClienteContatoMapper mapper;

    public ClienteContatoService(
            ClienteRepository clienteRepository,
            ClienteContatoRepository contatoRepository,
            ClienteContatoMapper mapper
    ) {
        this.clienteRepository = clienteRepository;
        this.contatoRepository = contatoRepository;
        this.mapper = mapper;
    }

    public List<ClienteContatoResponseDto> listar(Long clienteId) {
        return contatoRepository.findByClienteId(clienteId)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Transactional
    public ClienteContatoResponseDto criar(Long clienteId, ClienteContatoCreateRequestDto dto) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        ClienteContato contato = mapper.toEntity(dto);
        contato.setCliente(cliente);

        ClienteContato salvo = contatoRepository.save(contato);
        return mapper.toResponseDto(salvo);
    }

    @Transactional
    public ClienteContatoResponseDto atualizar(Long clienteId, Long contatoId, ClienteContatoCreateRequestDto dto) {
        ClienteContato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado"));

        if (!contato.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contato não pertence a este cliente");
        }

        mapper.updateFromDto(dto, contato);

        return mapper.toResponseDto(contato);
    }

    @Transactional
    public void deletar(Long clienteId, Long contatoId) {
        ClienteContato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contato não encontrado"));

        if (!contato.getCliente().getId().equals(clienteId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contato não pertence a este cliente");
        }

        contatoRepository.delete(contato);
    }

    @Transactional
    public ClienteEditarContatosRequestDto atualizarContatos(Long clienteId, ClienteEditarContatosRequestDto dto) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        cliente.getContatos().clear();

        if (dto.contatos() != null) {
            dto.contatos().forEach(c -> {
                ClienteContato contato = new ClienteContato();
                contato.setTipoContato(c.tipoContato());
                contato.setValor(c.valor());
                contato.setWhatsapp(c.whatsapp());
                contato.setObservacoes(c.observacoes());
                contato.setCliente(cliente);

                cliente.getContatos().add(contato);
            });
        }

        clienteRepository.save(cliente);

        return dto;
    }
}
