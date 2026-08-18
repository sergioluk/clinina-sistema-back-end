package com.clinina.sistema.service;

import com.clinina.sistema.dto.animal.response.AnimalCompletoResponseDto;
import com.clinina.sistema.dto.animal.response.AnimalMarcacaoResponseDto;
import com.clinina.sistema.dto.cliente.request.*;
import com.clinina.sistema.dto.cliente.response.*;
import com.clinina.sistema.mapper.ClienteMapper;
import com.clinina.sistema.model.entity.Cliente;
import com.clinina.sistema.model.entity.ClienteMarcacao;
import com.clinina.sistema.model.entity.Venda;
import com.clinina.sistema.model.entity.VendaPagamento;
import com.clinina.sistema.model.enums.VendaStatus;
import com.clinina.sistema.repository.ClienteRepository;
import com.clinina.sistema.repository.VendaRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final VendaRepository vendaRepository;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper, VendaRepository vendaRepository) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.vendaRepository = vendaRepository;
    }

    public List<ClienteBuscaResponseDto> listarClientes() {
        return this.clienteRepository.findAll().stream()
                .map(clienteMapper::toClienteListarResponseDto).toList();

    }

    @Transactional
    public ClienteCompletoResponseDto criarCliente(ClienteCreateRequestDto dto) {
        Cliente cliente = this.clienteMapper.toCliente(dto);

        if (cliente.getEndereco() != null) {
            cliente.getEndereco().setCliente(cliente);
        }

        if (cliente.getContatos() != null) {
            cliente.getContatos().forEach(c -> c.setCliente(cliente));
        }

        if (cliente.getMarcacoes() != null) {
            cliente.getMarcacoes().forEach(m -> m.setCliente(cliente));
        }

        if (cliente.getAnimais() != null) {
            cliente.getAnimais().forEach(animal -> animal.setCliente(cliente));
        }

        Cliente salvo = this.clienteRepository.save(cliente);
        return this.clienteMapper.toDtoCompleto(salvo);
    }

    public List<ClienteBuscaResponseDto> buscarClientesPorNome(String termo) {
        String termoNormalizado = normalize(termo);

        return clienteRepository.findTop20ByNomeNormalizadoContaining(termoNormalizado)
                .stream()
                .map(clienteMapper::toBuscaResponseDto)
                .toList();
    }

    public ClientePerfilResponseDto buscarClientePerfilPorId(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));

        BigDecimal saldo = buscarSaldoDevedorPorId(clienteId);

        return new ClientePerfilResponseDto(
                cliente.getId(),
                cliente.getNomeCompleto(),
                cliente.getNacionalidade(),
                cliente.getSexo(),
                cliente.getCpf(),
                cliente.getRg(),
                cliente.getAniversario(),
                cliente.getComoConheceu(),
                cliente.getProfissao(),
                saldo,
                cliente.getMarcacoes().stream()
                        .map(m -> new ClienteMarcacaoResponseDto(m.getId(), m.getNome(), m.getCliente().getId()))
                        .toList(),
                cliente.getObservacoes(),
                cliente.getContatos().stream()
                        .map(c -> new ClienteContatoResponseDto(
                                c.getId(),
                                c.getCliente().getId(),
                                c.getTipoContato(),
                                c.getValor(),
                                c.getWhatsapp(),
                                c.getObservacoes()
                        ))
                        .toList(),
                cliente.getEndereco() != null
                        ? new ClienteEnderecoResponseDto(
                                cliente.getEndereco().getId(),
                        cliente.getEndereco().getCliente().getId(),
                        cliente.getEndereco().getCep(),
                        cliente.getEndereco().getRua(),
                        cliente.getEndereco().getNumero(),
                        cliente.getEndereco().getComplemento(),
                        cliente.getEndereco().getBairro(),
                        cliente.getEndereco().getCidade(),
                        cliente.getEndereco().getEstado(),
                        cliente.getEndereco().getPontoReferencia()
                )
                        : null,
                cliente.getAnimais().stream()
                        .map(a -> new AnimalCompletoResponseDto(
                                a.getId(),
                                a.getCliente().getId(),
                                a.getNome(),
                                a.getEspecie(),
                                a.getSexo(),
                                a.getRaca(),
                                a.getEsterilizacao(),
                                a.getNascimento(),
                                a.getPelagem(),
                                a.getStatus(),
                                a.getFotoUrl(),
                                a.getMarcacoes().stream()
                                        .map(m -> new AnimalMarcacaoResponseDto(
                                                m.getId(),
                                                m.getNome(),
                                                m.getAnimal().getId()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
    }

    @Transactional
    public ClienteEditarDadosGeraisRequestDto atualizarDadosGerais(Long clienteId, ClienteEditarDadosGeraisRequestDto dto) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // atualiza campos simples
        cliente.setNomeCompleto(dto.nomeCompleto());
        cliente.setNacionalidade(dto.nacionalidade());
        cliente.setSexo(dto.sexo());
        cliente.setCpf(dto.cpf());
        cliente.setRg(dto.rg());
        cliente.setAniversario(dto.aniversario() != null ? LocalDate.parse(dto.aniversario()) : null);
        cliente.setComoConheceu(dto.comoNosConheceu());
        cliente.setObservacoes(dto.observacoes());

        // atualiza marcações: substitui completamente pelas novas
        if (dto.marcacoes() != null) {
            cliente.getMarcacoes().clear();
            dto.marcacoes().forEach(marcacaoDto -> {
                ClienteMarcacao marcacao = new ClienteMarcacao();
                marcacao.setNome(marcacaoDto.nome());
                marcacao.setCliente(cliente);
                cliente.getMarcacoes().add(marcacao);
            });
        }

        clienteRepository.save(cliente);

        List<ClienteMarcacaoCreateRequestDto> marcacoesAtualizadas = cliente.getMarcacoes()
                .stream()
                .map(m -> new ClienteMarcacaoCreateRequestDto(m.getNome()))
                .toList();

        return new ClienteEditarDadosGeraisRequestDto(
                cliente.getNomeCompleto(),
                cliente.getNacionalidade(),
                cliente.getSexo(),
                cliente.getCpf(),
                cliente.getRg(),
                cliente.getAniversario() != null ? cliente.getAniversario().toString() : null,
                cliente.getComoConheceu(),
                marcacoesAtualizadas,
                cliente.getObservacoes()
        );
    }

    private String normalize(String texto) {
        return java.text.Normalizer
                .normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .toLowerCase()
                .trim();
    }

    public ClientePdvResponseDto buscarClientePdvPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));

        ClientePdvResponseDto dto = clienteMapper.toPdvResponseDto(cliente);

        BigDecimal saldoDevedor = buscarSaldoDevedorPorId(id);

        return new ClientePdvResponseDto(
                dto.id(),
                dto.nomeCompleto(),
                saldoDevedor,
                dto.marcacoes(),
                dto.animais()
        );
    }

    public BigDecimal buscarSaldoDevedorPorId(Long clienteId) {
        List<VendaStatus> statusPermitidos = List.of(VendaStatus.ABERTA, VendaStatus.EM_ATENDIMENTO, VendaStatus.PARCIAL);
        List<Venda> vendas = vendaRepository.findByClienteIdAndStatusIn(clienteId, statusPermitidos);

        return vendas.stream()
                .map(this::calcularSaldoRestante)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularSaldoRestante(Venda venda) {
        BigDecimal totalPago = venda.getPagamentos().stream()
                .map(VendaPagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return venda.getValorTotal().subtract(totalPago);
    }

}
