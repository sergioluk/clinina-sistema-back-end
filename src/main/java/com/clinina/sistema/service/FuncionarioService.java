package com.clinina.sistema.service;

import com.clinina.sistema.dto.funcionario.request.FuncionarioCreateRequestDto;
import com.clinina.sistema.dto.funcionario.response.FuncionarioAtivoResponseDto;
import com.clinina.sistema.dto.funcionario.response.FuncionarioResponseDto;
import com.clinina.sistema.mapper.FuncionarioMapper;
import com.clinina.sistema.model.entity.Funcionario;
import com.clinina.sistema.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioMapper funcionarioMapper;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            FuncionarioMapper funcionarioMapper
    ) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioMapper = funcionarioMapper;
    }

    public List<FuncionarioResponseDto> listar() {
        return funcionarioRepository.findAll()
                .stream()
                .map(funcionarioMapper::toResponseDto)
                .toList();
    }

    public List<FuncionarioAtivoResponseDto> listarAtivos() {
        return funcionarioRepository.findByAtivoTrueOrderByNomeAsc()
                .stream()
                .map(funcionarioMapper::toAtivoResponseDto)
                .toList();
    }

    public FuncionarioResponseDto buscarPorId(Long id) {
        Funcionario funcionario = buscarEntidadePorId(id);
        return funcionarioMapper.toResponseDto(funcionario);
    }

    @Transactional
    public FuncionarioResponseDto criar(FuncionarioCreateRequestDto dto) {
        Funcionario funcionario = funcionarioMapper.toEntity(dto);
        funcionario.setAtivo(true);

        Funcionario salvo = funcionarioRepository.save(funcionario);

        return funcionarioMapper.toResponseDto(salvo);
    }

    @Transactional
    public FuncionarioResponseDto atualizar(Long id, FuncionarioCreateRequestDto dto) {
        Funcionario funcionario = buscarEntidadePorId(id);

        funcionarioMapper.updateFromDto(dto, funcionario);

        return funcionarioMapper.toResponseDto(funcionario);
    }

    @Transactional
    public void desativar(Long id) {
        Funcionario funcionario = buscarEntidadePorId(id);
        funcionario.setAtivo(false);
    }

    private Funcionario buscarEntidadePorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Funcionário não encontrado"
                ));
    }
}
