package com.clinina.sistema.service;

import com.clinina.sistema.dto.caixa.request.CaixaCreateRequestDto;
import com.clinina.sistema.dto.caixa.request.CaixaEncerrarRequestDto;
import com.clinina.sistema.dto.caixa.response.CaixaAtualUsuarioResponseDto;
import com.clinina.sistema.dto.caixa.response.CaixaDetalheResponseDto;
import com.clinina.sistema.dto.caixa.response.CaixaListaDiaDto;
import com.clinina.sistema.dto.caixa.response.CaixaResponseDto;
import com.clinina.sistema.mapper.CaixaMapper;
import com.clinina.sistema.model.entity.*;
import com.clinina.sistema.model.enums.StatusCaixa;
import com.clinina.sistema.model.enums.TipoMovimentacao;
import com.clinina.sistema.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final CaixaMapper caixaMapper;
    private final ContaRepository contaRepository;
    private final CaixaMovimentacaoRepository caixaMovimentacaoRepository;
    private final VendaPagamentoRepository vendaPagamentoRepository;
    private final FormaRecebimentoRepository formaRecebimentoRepository;

    public CaixaService(
            CaixaRepository caixaRepository,
            FuncionarioRepository funcionarioRepository,
            CaixaMapper caixaMapper, ContaRepository contaRepository, CaixaMovimentacaoRepository caixaMovimentacaoRepository, VendaPagamentoRepository vendaPagamentoRepository, FormaRecebimentoRepository formaRecebimentoRepository
    ) {
        this.caixaRepository = caixaRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.caixaMapper = caixaMapper;
        this.contaRepository = contaRepository;
        this.caixaMovimentacaoRepository = caixaMovimentacaoRepository;
        this.vendaPagamentoRepository = vendaPagamentoRepository;
        this.formaRecebimentoRepository = formaRecebimentoRepository;
    }

    public List<CaixaResponseDto> listar() {
        return caixaRepository.findAll()
                .stream()
                .map(caixaMapper::toResponseDto)
                .toList();
    }

    public CaixaResponseDto buscarPorId(Long id) {
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));
        return caixaMapper.toResponseDto(caixa);
    }

    @Transactional
    public CaixaResponseDto criar(CaixaCreateRequestDto dto) {
        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionario não encontrado"));

        Caixa caixa = new Caixa();
        caixa.setNome(dto.nome());
        caixa.setFuncionario(funcionario);
        caixa.setStatus(StatusCaixa.ABERTO);
        caixa.setDataHoraAbertura(Instant.now());

        Caixa salvo = caixaRepository.save(caixa);
        return caixaMapper.toResponseDto(salvo);
    }

    @Transactional
    public CaixaResponseDto atualizarStatus(Long id, StatusCaixa status) {
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));

        caixa.setStatus(status);
        if (status == StatusCaixa.FECHADO || status == StatusCaixa.FINALIZADO) {
            caixa.setDataHoraFechamento(Instant.now());
        }

        return caixaMapper.toResponseDto(caixa);
    }

    @Transactional
    public void deletar(Long id) {
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));

        caixaRepository.delete(caixa);
    }


    @Transactional
    public CaixaResponseDto abrirCaixa(CaixaCreateRequestDto dto) {

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        caixaRepository.findCaixaAtualDoFuncionario(dto.funcionarioId())
                .ifPresent(caixa -> {
                    throw new ResponseStatusException(
                     HttpStatus.BAD_REQUEST,
                    "Funcionário já possui um caixa " + caixa.getStatus());
        });

        Caixa caixa = new Caixa();
        caixa.setFuncionario(funcionario);
        caixa.setNome("Caixa - " + funcionario.getNome());
        caixa.setDataHoraAbertura(Instant.now());
        caixa.setStatus(StatusCaixa.ABERTO);

        Caixa salvo = caixaRepository.save(caixa);


        //se houver suprimento inicial, criar movimentação e transferir valor da conta de origem
        if (dto.valorSuprimento() != null && dto.valorSuprimento().compareTo(BigDecimal.ZERO) > 0) {

            Conta contaOrigem = contaRepository.findById(dto.contaOrigemId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de origem não encontrada"));

            FormaRecebimento forma = formaRecebimentoRepository.findById(dto.formaRecebimentoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de recebimento não encontrada"));

            // movimentação de suprimento
            CaixaMovimentacao mov = new CaixaMovimentacao();
            mov.setCaixa(salvo);
            mov.setDataHora(Instant.now());
            mov.setTipo(TipoMovimentacao.SUPRIMENTO);
            mov.setDescricao(dto.descricaoSuprimento());
            mov.setConta(contaOrigem);
            mov.setUsuario(funcionario);
            mov.setValor(dto.valorSuprimento());
            mov.setFormaRecebimento(forma);
            caixaMovimentacaoRepository.save(mov);

            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(dto.valorSuprimento()));

            contaRepository.save(contaOrigem);
        }



        return caixaMapper.toResponseDto(salvo);
    }

    @Transactional
    public List<CaixaListaDiaDto> listarCaixasPorDia(LocalDate dia) {
        //converte LocalDate para Instant início e fim do dia
        Instant inicioDoDia = dia.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant fimDoDia = dia.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Caixa> caixas = caixaRepository.findByDataHoraAberturaBetween(inicioDoDia, fimDoDia);

        return caixas.stream()
                .map(caixa -> new CaixaListaDiaDto(
                        caixa.getId(),
                        caixa.getDataHoraAbertura(),
                        caixa.getDataHoraFechamento(),
                        caixa.getDataHoraEncerramento(),
                        caixa.getStatus(),
                        new CaixaListaDiaDto.FuncionarioResumoDto(
                                caixa.getFuncionario().getId(),
                                caixa.getFuncionario().getNome()
                        )
                ))
                .toList();
    }

    @Transactional
    public CaixaDetalheResponseDto detalhesCaixa(Long caixaId) {
        Caixa caixa = caixaRepository.findById(caixaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));

        List<CaixaMovimentacao> movimentacoes = caixaMovimentacaoRepository.findByCaixaId(caixaId);

        List<CaixaDetalheResponseDto.CaixaMovimentacaoDto> movimentacoesDto = movimentacoes.stream()
                .map(m -> new CaixaDetalheResponseDto.CaixaMovimentacaoDto(
                        m.getDataHora(),
                        m.getTipo(),
                        m.getDescricao(),
                        m.getConta() != null ? m.getConta().getNome() : null,
                        m.getUsuario() != null ? m.getUsuario().getNome() : null,
                        m.getFormaRecebimento() != null ? m.getFormaRecebimento().getNome() : null,
                        m.getValor()
                ))
                .toList();

        List<VendaPagamento> pagamentos = vendaPagamentoRepository.findPagamentosPorCaixa(caixaId);

        //agrupar por forma de recebimento para resumo
        Map<String, List<VendaPagamento>> pagamentosPorForma = pagamentos.stream()
                .collect(Collectors.groupingBy(p -> p.getFormaRecebimento().getNome()));

        List<CaixaDetalheResponseDto.CaixaResumoRecebimentoDto> resumoRecebimentos = pagamentosPorForma.entrySet().stream()
                .map(entry -> {
                    String forma = entry.getKey();

                    BigDecimal totalVendas = entry.getValue().stream()
                            .map(VendaPagamento::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalSuprimentos = movimentacoes.stream()
                            .filter(m -> m.getTipo().equals(TipoMovimentacao.SUPRIMENTO) &&
                                    forma.equalsIgnoreCase(m.getFormaRecebimento() != null ? m.getFormaRecebimento().getNome() : ""))
                            .map(CaixaMovimentacao::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalSangrias = movimentacoes.stream()
                            .filter(m -> m.getTipo().equals(TipoMovimentacao.SANGRIA) &&
                                    forma.equalsIgnoreCase(m.getFormaRecebimento() != null ? m.getFormaRecebimento().getNome() : ""))
                            .map(CaixaMovimentacao::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalDespesas = movimentacoes.stream()
                            .filter(m -> m.getTipo().equals(TipoMovimentacao.DESPESA) &&
                                    forma.equalsIgnoreCase(m.getFormaRecebimento() != null ? m.getFormaRecebimento().getNome() : ""))
                            .map(CaixaMovimentacao::getValor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal resultado = totalVendas.add(totalSuprimentos).add(totalSangrias).add(totalDespesas);

                    return new CaixaDetalheResponseDto.CaixaResumoRecebimentoDto(
                            forma,
                            totalVendas,
                            totalSuprimentos,
                            totalSangrias,
                            totalDespesas,
                            resultado
                    );
                }).toList();

        List<CaixaDetalheResponseDto.CaixaRecebimentoVendaDto> recebimentosVendas = montarRecebimentosVendas(caixaId);

        return new CaixaDetalheResponseDto(
                caixa.getId(),
                caixa.getDataHoraAbertura(),
                caixa.getDataHoraFechamento(),
                caixa.getDataHoraEncerramento(),
                caixa.getStatus(),
                new CaixaDetalheResponseDto.FuncionarioResumoDto(
                        caixa.getFuncionario().getId(),
                        caixa.getFuncionario().getNome()
                ),
                resumoRecebimentos,
                movimentacoesDto,
                recebimentosVendas
        );
    }

    @Transactional
    public List<CaixaDetalheResponseDto.CaixaRecebimentoVendaDto> montarRecebimentosVendas(Long caixaId) {

        List<VendaPagamento> pagamentos = vendaPagamentoRepository.findPagamentosPorCaixa(caixaId);

        Map<Long, List<VendaPagamento>> pagamentosPorVenda = pagamentos.stream()
                .collect(Collectors.groupingBy(p -> p.getVenda().getId()));

        List<CaixaDetalheResponseDto.CaixaRecebimentoVendaDto> result = pagamentosPorVenda.entrySet().stream().map(entry -> {

            Long vendaId = entry.getKey();
            List<VendaPagamento> pagamentosVenda = entry.getValue();

            // Venda associada
            var venda = pagamentosVenda.get(0).getVenda();

            // Data da venda
            Instant dataVenda = venda.getDataHoraVenda();

            // Última baixa
            Instant ultimaBaixa = pagamentosVenda.stream()
                    .map(VendaPagamento::getDataPagamento)
                    .max(Instant::compareTo)
                    .orElse(null);

            // Cliente
            Long clienteId = venda.getCliente() != null ? venda.getCliente().getId() : null;
            String clienteNome = venda.getCliente() != null ? venda.getCliente().getNomeCompleto() : null;

            // Total pago até agora
            BigDecimal valorPagoTotal = pagamentosVenda.stream()
                    .map(VendaPagamento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Lista das formas de pagamento
            List<CaixaDetalheResponseDto.CaixaRecebimentoVendaDto.FormaPagamentoDto> formas = pagamentosVenda.stream()
                    .map(p -> new CaixaDetalheResponseDto.CaixaRecebimentoVendaDto.FormaPagamentoDto(
                            p.getFormaRecebimento().getNome(),
                            p.getValor(),
                            p.getQuantidadeParcelas() != null ? p.getQuantidadeParcelas() : 1
                    ))
                    .toList();

            return new CaixaDetalheResponseDto.CaixaRecebimentoVendaDto(
                    vendaId,
                    dataVenda,
                    ultimaBaixa,
                    clienteId,
                    clienteNome,
                    valorPagoTotal,
                    formas,
                    venda.getStatus()
            );

        }).toList();

        return result;
    }

    @Transactional
    public CaixaResponseDto fecharCaixa(Long caixaId) {
        Caixa caixa = caixaRepository.findById(caixaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));

        caixa.setStatus(StatusCaixa.FECHADO);
        caixa.setDataHoraFechamento(Instant.now());

        Caixa salvo = caixaRepository.save(caixa);

        return caixaMapper.toResponseDto(salvo);
    }

    @Transactional
    public CaixaResponseDto buscarCaixaDoUsuario(Long usuarioId) {

        return caixaRepository.findCaixaAtualDoFuncionario(usuarioId)
                .map(caixaMapper::toResponseDto)
                .orElse(null);
    }

    @Transactional
    public CaixaAtualUsuarioResponseDto buscarCaixaAtualDoUsuario(Long usuarioId) {

        Optional<Caixa> optionalCaixa = caixaRepository.findCaixaAtualDoFuncionario(usuarioId);

        if (optionalCaixa.isEmpty()) {
            return new CaixaAtualUsuarioResponseDto(
                    null,
                    false,
                    "ABRIR"
            );
        }

        Caixa caixa = optionalCaixa.get();

        boolean precisaFinalizar = false;

        if (caixa.getStatus() == StatusCaixa.ABERTO ||
                caixa.getStatus() == StatusCaixa.FECHADO) {

            ZoneId zone = ZoneId.of("America/Sao_Paulo");
            LocalDate hoje = LocalDate.now(zone);
            Instant inicioDoDia = hoje.atStartOfDay(zone).toInstant();
            Instant inicioDoProximoDia = hoje.plusDays(1).atStartOfDay(zone).toInstant();
            precisaFinalizar = caixa.getDataHoraAbertura().isBefore(inicioDoDia) || !caixa.getDataHoraAbertura().isBefore(inicioDoProximoDia);
        }

        String acaoDisponivel;

        if (caixa.getStatus() == StatusCaixa.ABERTO) {
            acaoDisponivel = "FECHAR";
        } else if (caixa.getStatus() == StatusCaixa.FECHADO) {
            acaoDisponivel = "REABRIR";
        } else {
            acaoDisponivel = "ABRIR";
        }

        return new CaixaAtualUsuarioResponseDto(caixaMapper.toResponseDto(caixa), precisaFinalizar, acaoDisponivel);
    }

    @Transactional
    public String finalizarCaixa(Long caixaId, CaixaEncerrarRequestDto dto) {

        Caixa caixa = caixaRepository.findById(caixaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        FormaRecebimento formaDinheiro = formaRecebimentoRepository.findByNome("Dinheiro")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de recebimento 'Dinheiro' não encontrada"));

        // Encontrar conta de destino
        Conta contaDestino = contaRepository.findById(dto.contaDestinoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de destino não encontrada"));

        BigDecimal saldoCaixa = obterSaldo(caixaId);

        CaixaMovimentacao mov = new CaixaMovimentacao();
        mov.setCaixa(caixa);
        mov.setUsuario(funcionario);
        mov.setConta(contaDestino);
        mov.setValor(saldoCaixa);
        mov.setDescricao(dto.comentario());
        mov.setTipo(TipoMovimentacao.ENCERRAMENTO);
        mov.setFormaRecebimento(formaDinheiro);

        // Atualizar status do caixa e data de encerramento
        LocalDateTime dataHoraEncerramento = LocalDateTime.of(dto.data(), dto.hora());
        caixa.setStatus(StatusCaixa.FINALIZADO);
        caixa.setDataHoraEncerramento(dataHoraEncerramento.atZone(ZoneId.systemDefault()).toInstant());

        mov.setDataHora(dataHoraEncerramento.atZone(ZoneId.systemDefault()).toInstant());


        caixaMovimentacaoRepository.save(mov);

        contaDestino.setSaldo(contaDestino.getSaldo().add(saldoCaixa));

        contaRepository.save(contaDestino);

        caixa.setComentario(dto.comentario());

        caixaRepository.save(caixa);

        return "Caixa de id " + caixaId + " encerrado com sucesso. Saldo transferido: " + saldoCaixa;
    }

    @Transactional
    public BigDecimal obterSaldo(Long caixaId) {
        BigDecimal vendasDinheiro = vendaPagamentoRepository.somarDinheiroVendasPorCaixa(caixaId);
        BigDecimal movimentacoesDinheiro = caixaMovimentacaoRepository.calcularMovimentacoesDinheiroPorCaixa(caixaId);

        return vendasDinheiro.add(movimentacoesDinheiro);
    }

    @Transactional
    public CaixaResponseDto reabrirCaixa(Long id) {

        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Caixa não encontrado"
                ));

        if (caixa.getStatus() == StatusCaixa.FINALIZADO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não é possível reabrir um caixa finalizado"
            );
        }

        if (caixa.getStatus() == StatusCaixa.ABERTO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O caixa já está aberto"
            );
        }

        caixa.setStatus(StatusCaixa.ABERTO);
        caixa.setDataHoraFechamento(null);
        return caixaMapper.toResponseDto(caixa);
    }

    @Transactional
    public List<CaixaResponseDto> listarCaixasAbertosFechados() {

        List<StatusCaixa> statusPermitidos = List.of(StatusCaixa.ABERTO, StatusCaixa.FECHADO);

        List<Caixa> caixas = caixaRepository.findByStatusIn(statusPermitidos);

        return caixas.stream()
                .map(caixaMapper::toResponseDto)
                .toList();
    }
}
