package com.clinina.sistema.service;

import com.clinina.sistema.dto.animal.response.AnimalCompletoResponseDto;
import com.clinina.sistema.dto.animal.request.AnimalCreateRequestDto;
import com.clinina.sistema.dto.animal.response.AnimalPesquisarClienteResponseDto;
import com.clinina.sistema.mapper.AnimalMapper;
import com.clinina.sistema.model.entity.Animal;
import com.clinina.sistema.model.entity.AnimalMarcacao;
import com.clinina.sistema.model.entity.Cliente;
import com.clinina.sistema.model.enums.StatusAnimal;
import com.clinina.sistema.repository.AnimalRepository;
import com.clinina.sistema.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ClienteRepository clienteRepository;
    private final AnimalMapper animalMapper;

    public AnimalService(
            AnimalRepository animalRepository,
            ClienteRepository clienteRepository,
            AnimalMapper animalMapper
    ) {
        this.animalRepository = animalRepository;
        this.clienteRepository = clienteRepository;
        this.animalMapper = animalMapper;
    }

    public List<AnimalCompletoResponseDto> listarPorCliente(Long clienteId) {
        return animalRepository.findByClienteId(clienteId)
                .stream()
                .map(animalMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public AnimalCompletoResponseDto criar(AnimalCreateRequestDto dto) {

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));

        Animal animal = animalMapper.toEntity(dto);

        animal.setCliente(cliente);

        if (animal.getStatus() == null) {
            animal.setStatus(StatusAnimal.VIVO);
        }

        if (dto.marcacoes() != null) {
            dto.marcacoes().forEach(m -> {
                AnimalMarcacao marcacao = new AnimalMarcacao();
                marcacao.setNome(m.nome());
                marcacao.setAnimal(animal);
                animal.getMarcacoes().add(marcacao);
            });
        }

        Animal salvo = animalRepository.save(animal);

        return animalMapper.toResponseDto(salvo);
    }

    @Transactional
    public AnimalCompletoResponseDto atualizar(Long id, AnimalCreateRequestDto dto) {

        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Animal não encontrado"
                ));

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));

        animalMapper.updateFromDto(dto, animal);

        animal.setCliente(cliente);

        if (dto.marcacoes() != null) {
            animal.getMarcacoes().clear();
            dto.marcacoes().forEach(m -> {
                AnimalMarcacao marcacao = new AnimalMarcacao();
                marcacao.setNome(m.nome());
                marcacao.setAnimal(animal);
                animal.getMarcacoes().add(marcacao);
            });
        }

        Animal salvo = animalRepository.save(animal);

        return animalMapper.toResponseDto(salvo);
    }

    public List<AnimalCompletoResponseDto> listar() {
        return animalRepository.findAll()
                .stream()
                .map(animalMapper::toResponseDto)
                .toList();
    }

    public AnimalCompletoResponseDto buscarPorId(Long id) {

        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Animal não encontrado"
                ));

        return animalMapper.toResponseDto(animal);
    }

    @Transactional
    public void deletar(Long id) {

        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Animal não encontrado"
                ));

        animalRepository.delete(animal);
    }

    public List<AnimalPesquisarClienteResponseDto> buscarAnimaisPorNome(String termo) {
        String termoNormalizado = normalize(termo);

        return animalRepository.findTop20ByNomeNormalizadoContaining(termoNormalizado)
                .stream()
                .map(a -> new AnimalPesquisarClienteResponseDto(
                        a.getId(),
                        a.getNome(),
                        a.getCliente() != null ? a.getCliente().getId() : null,
                        a.getCliente() != null ? a.getCliente().getNomeCompleto() : null
                ))
                .toList();
    }

    private String normalize(String texto) {
        return java.text.Normalizer
                .normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .toLowerCase()
                .trim();
    }

}
