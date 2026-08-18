package com.clinina.sistema.service;

import com.clinina.sistema.dto.conta.request.ContaCreateRequestDto;
import com.clinina.sistema.dto.conta.response.ContaResponseDto;
import com.clinina.sistema.model.entity.Caixa;
import com.clinina.sistema.model.entity.Conta;
import com.clinina.sistema.model.entity.Funcionario;
import com.clinina.sistema.repository.CaixaRepository;
import com.clinina.sistema.repository.ContaRepository;
import com.clinina.sistema.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final CaixaRepository caixaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public ContaService(ContaRepository contaRepository, CaixaRepository caixaRepository, FuncionarioRepository funcionarioRepository) {
        this.contaRepository = contaRepository;
        this.caixaRepository = caixaRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<ContaResponseDto> listar() {
        return contaRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public ContaResponseDto buscarPorId(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        return toDto(conta);
    }

    @Transactional
    public ContaResponseDto criar(ContaCreateRequestDto dto) {
        Conta conta = new Conta();
        conta.setNome(dto.nome());
        conta.setTipo(dto.tipo());
        conta.setStatus(dto.status() != null ? dto.status() : true);
        conta.setPermitirLancamentosRapidos(dto.permitirLancamentosRapidos() != null ? dto.permitirLancamentosRapidos() : false);
        conta.setDataCriacao(dto.dataCriacao() != null ? dto.dataCriacao() : Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
        conta.setSaldo(dto.saldo() != null ? dto.saldo() : BigDecimal.ZERO);
        conta.setSituacao(dto.situacao() != null ? dto.situacao() : com.clinina.sistema.model.enums.SituacaoConta.POSITIVO);

        Conta salvo = contaRepository.save(conta);
        return toDto(salvo);
    }

    @Transactional
    public ContaResponseDto atualizar(Long id, ContaCreateRequestDto dto) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        conta.setNome(dto.nome());
        conta.setTipo(dto.tipo());
        conta.setStatus(dto.status() != null ? dto.status() : conta.getStatus());
        conta.setPermitirLancamentosRapidos(dto.permitirLancamentosRapidos() != null ? dto.permitirLancamentosRapidos() : conta.getPermitirLancamentosRapidos());
        conta.setDataCriacao(dto.dataCriacao() != null ? dto.dataCriacao() : conta.getDataCriacao());
        conta.setSaldo(dto.saldo() != null ? dto.saldo() : conta.getSaldo());
        conta.setSituacao(dto.situacao() != null ? dto.situacao() : conta.getSituacao());

        Conta salvo = contaRepository.save(conta);
        return toDto(salvo);
    }

    @Transactional
    public void deletar(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        contaRepository.delete(conta);
    }

    @Transactional
    public BigDecimal getSaldoContaPorCaixaFuncionario(Long caixaId, Long funcionarioId) {

        Caixa caixa = caixaRepository.findById(caixaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));

        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        String nomeConta = "Caixa - " + funcionario.getNome() + " (" + caixa.getId() + ")";

        Conta contaCaixa = contaRepository.findByNome(nomeConta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Conta do caixa não encontrada"));

        return contaCaixa.getSaldo();
    }

    private ContaResponseDto toDto(Conta conta) {
        return new ContaResponseDto(
                conta.getId(),
                conta.getNome(),
                conta.getTipo(),
                conta.getStatus(),
                conta.getPermitirLancamentosRapidos(),
                conta.getDataCriacao(),
                conta.getSaldo(),
                conta.getSituacao()
        );
    }
}
