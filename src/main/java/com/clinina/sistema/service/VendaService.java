package com.clinina.sistema.service;

import com.clinina.sistema.dto.venda.request.*;
import com.clinina.sistema.dto.venda.response.*;
import com.clinina.sistema.mapper.VendaMapper;
import com.clinina.sistema.model.entity.*;
import com.clinina.sistema.model.enums.StatusPagamento;
import com.clinina.sistema.model.enums.VendaStatus;
import com.clinina.sistema.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final AnimalRepository animalRepository;
    private final ProdutosServicosRepository produtoRepository;
    private final FormaRecebimentoRepository formaRecebimentoRepository;
    private final VendaMapper vendaMapper;
    private final FuncionarioRepository funcionarioRepository;
    private final CaixaRepository caixaRepository;

    public VendaService(
            VendaRepository vendaRepository,
            ClienteRepository clienteRepository,
            AnimalRepository animalRepository,
            ProdutosServicosRepository produtoRepository,
            FormaRecebimentoRepository formaRecebimentoRepository,
            VendaMapper vendaMapper, FuncionarioRepository funcionarioRepository, CaixaRepository caixaRepository
    ) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.animalRepository = animalRepository;
        this.produtoRepository = produtoRepository;
        this.formaRecebimentoRepository = formaRecebimentoRepository;
        this.vendaMapper = vendaMapper;
        this.funcionarioRepository = funcionarioRepository;
        this.caixaRepository = caixaRepository;
    }

    @Transactional
    public VendaResponseDto criarVendaEmAtendimento(VendaCreateOpenRequestDto dto) {
        Venda venda = new Venda();

        if (dto.dataHoraVenda() != null) {
            venda.setDataHoraVenda(dto.dataHoraVenda().atZone(ZoneId.of("America/Fortaleza")).toInstant());
        } else {
            venda.setDataHoraVenda(Instant.now());
        }

        venda.setStatus(VendaStatus.EM_ATENDIMENTO); // inicia como em atendimento

        if (dto.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.clienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
            venda.setCliente(cliente);
        }

        if (dto.caixaId() != null) {
            Caixa caixa = caixaRepository.findById(dto.caixaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));
            venda.setCaixa(caixa);
        }

        // monta itens da venda
        List<VendaItem> itens = montarItens(dto.itens(), venda);
        venda.setItens(itens);

        // calcula valor bruto e total de desconto
        BigDecimal valorBruto = itens.stream()
                .map(i -> i.getValorUnitario().multiply(i.getQuantidade()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal descontoItens = itens.stream()
                .map(VendaItem::getValorDesconto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        venda.setValorBruto(valorBruto);
        venda.setValorDesconto(descontoItens);
        venda.setValorTotal(valorBruto.subtract(descontoItens));

        venda.setPagamentos(new ArrayList<>());

        venda.setObservacoes(dto.observacoes());

        Venda salvo = vendaRepository.save(venda);

        return vendaMapper.toResponseDto(salvo);
    }

    public VendaResponseDto buscarVendaEmAtendimentoPorCliente(Long clienteId) {
        return vendaRepository
                .findFirstByClienteIdAndStatus(clienteId, VendaStatus.EM_ATENDIMENTO)
                .map(vendaMapper::toResponseDto)
                .orElseGet(() -> null);
    }


    @Transactional
    public VendaResponseDto alterarStatusVenda(Long vendaId, AtualizarStatusVendaRequestDto dto) {

        Venda venda = vendaRepository.findById(vendaId).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Venda não encontrada"));

        if(venda.getStatus() == VendaStatus.FINALIZADA || venda.getStatus() == VendaStatus.PARCIAL) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Venda finalizada não pode ser editada. Cancele a venda e faça uma nova."
            );
        }

        //guarda os itens antigos para comparar estoque
        Map<Long, BigDecimal> estoqueAntigo = venda.getItens()
                .stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduto().getId(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                VendaItem::getQuantidade,
                                BigDecimal::add
                        )
                ));

        //monta mapa dos novos itens
        Map<Long, BigDecimal> estoqueNovo = dto.itens()
                .stream()
                .collect(Collectors.groupingBy(
                        item -> item.produtoId(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                AtualizarStatusVendaRequestDto.VendaItemDto::quantidade,
                                BigDecimal::add
                        )
                ));

         //junta produtos antigos e novos Isso resolve o caso: (Antes: Ração 2 Sachê 1) (Depois: Sachê 1) A ração volta para estoque.
        Set<Long> produtosAlterados = new HashSet<>();
        produtosAlterados.addAll(estoqueAntigo.keySet());
        produtosAlterados.addAll(estoqueNovo.keySet());

        //ajusta estoque
        for (Long produtoId : produtosAlterados) {
            BigDecimal quantidadeAntiga = estoqueAntigo.getOrDefault(produtoId, BigDecimal.ZERO);
            BigDecimal quantidadeNova = estoqueNovo.getOrDefault(produtoId, BigDecimal.ZERO);

            //Exemplo: (Antes: 2 Depois: 3 diferença = +1 baixa mais 1)
            //(Antes: 2 Depois: 1 diferença = -1 devolve 1)
            BigDecimal diferenca = quantidadeNova.subtract(quantidadeAntiga);

            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Produto não encontrado"
                    ));

            if (Boolean.TRUE.equals(produto.getControlaEstoque())) {
                BigDecimal estoqueAtual = produto.getEstoqueAtual() != null ? produto.getEstoqueAtual() : BigDecimal.ZERO;
                produto.setEstoqueAtual(estoqueAtual.subtract(diferenca));
                produtoRepository.save(produto);
            }
        }

        //remove itens antigos
        venda.getItens().clear();

        //cria os novos itens
        List<VendaItem> novosItens = dto.itens()
                .stream()
                .map(itemDto -> {

                    Produto produto = produtoRepository.findById(itemDto.produtoId())
                            .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Produto não encontrado"
                            )
                    );

                    VendaItem item = new VendaItem();

                    item.setVenda(venda);
                    item.setProduto(produto);
                    item.setDescricao(produto.getNome());

                    item.setQuantidade(itemDto.quantidade());

                    item.setValorUnitario(itemDto.valorUnitario());

                    BigDecimal desconto = itemDto.valorDesconto() != null ? itemDto.valorDesconto() : BigDecimal.ZERO;

                    item.setValorDesconto(desconto);

                    item.setValorTotal(itemDto.valorUnitario().multiply(itemDto.quantidade()).subtract(desconto));


                    if (itemDto.animalId() != null) {

                        Animal animal = animalRepository.findById(itemDto.animalId()).orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Animal não encontrado"
                                )
                        );

                        item.setAnimal(animal);
                    }

                    if (itemDto.funcionarioId() != null) {

                        Funcionario funcionario = funcionarioRepository.findById(itemDto.funcionarioId()).orElseThrow(() ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Funcionário não encontrado"
                                        )
                                );

                        item.setFuncionario(funcionario);
                    }

                    return item;

                })
                .toList();

        venda.getItens().addAll(novosItens);

        venda.setStatus(dto.status());

        //recalcula valores
        BigDecimal valorBruto = novosItens.stream()
                .map(item ->
                        item.getValorUnitario()
                                .multiply(item.getQuantidade())
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        BigDecimal valorDesconto = novosItens.stream()
                .map(VendaItem::getValorDesconto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        venda.setValorBruto(valorBruto);
        venda.setValorDesconto(valorDesconto);
        venda.setValorTotal(valorBruto.subtract(valorDesconto));

        Venda salva = vendaRepository.save(venda);

        return vendaMapper.toResponseDto(salva);
    }

    @Transactional
    public List<VendaResponseDto> finalizarVendas(VendaFinalizeCreateRequestDto dto) {

        // Agrupa os pagamentos por vendaId
        Map<Long, List<VendasPagamentosFinalizeRequestDto>> pagamentosPorVenda =
                dto.pagamentos().stream()
                        .collect(Collectors.groupingBy(VendasPagamentosFinalizeRequestDto::vendaId));

        List<VendaResponseDto> result = new ArrayList<>();

        for (Map.Entry<Long, List<VendasPagamentosFinalizeRequestDto>> entry : pagamentosPorVenda.entrySet()) {

            Long vendaId = entry.getKey();
            List<VendasPagamentosFinalizeRequestDto> pagamentosDto = entry.getValue();

            Venda venda = vendaRepository.findById(vendaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada"));

            if (venda.getStatus() != VendaStatus.ABERTA &&
                    venda.getStatus() != VendaStatus.EM_ATENDIMENTO &&
                    venda.getStatus() != VendaStatus.PARCIAL) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Só é possível pagar vendas com status ABERTA, EM_ATENDIMENTO ou PARCIAL");
            }

            // condição para venda parcial
            boolean isParcial = pagamentosDto.size() == 1 &&
                    pagamentosDto.stream().map(VendasPagamentosFinalizeRequestDto::valor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .compareTo(venda.getValorTotal()) < 0;

            // se tentativa de venda parcial em múltiplas vendas
            if (isParcial && pagamentosPorVenda.size() > 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Pagamentos parciais só podem ser feitos em uma venda por vez");
            }

            // monta pagamentos
            List<VendaPagamento> pagamentos = montarPagamentosFinalizar(pagamentosDto, venda);

            if (venda.getPagamentos() == null) {
                venda.setPagamentos(new ArrayList<>());
            }
            venda.getPagamentos().addAll(pagamentos);

            // calcula total pago acumulado
            BigDecimal totalPago = venda.getPagamentos().stream()
                    .map(VendaPagamento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalPago.compareTo(venda.getValorTotal()) < 0) {
                venda.setStatus(VendaStatus.PARCIAL);
            } else {
                venda.setStatus(VendaStatus.FINALIZADA);
            }

            Venda salvo = vendaRepository.save(venda);
            result.add(vendaMapper.toResponseDto(salvo));
        }

        return result;
    }

    @Transactional
    public VendaResponseDto criarVenda(VendaCreateRequestDto dto) {
        Venda venda = new Venda();

        venda.setDataHoraVenda(Instant.now());
        venda.setStatus(VendaStatus.FINALIZADA);

        if (dto.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.clienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
            venda.setCliente(cliente);
        }

        if (dto.caixaId() != null) {
            Caixa caixa = caixaRepository.findById(dto.caixaId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Caixa não encontrado"
                    ));
            venda.setCaixa(caixa);
        }

        List<VendaItem> itens = montarItens(dto.itens(), venda);
        venda.setItens(itens);

        BigDecimal valorBruto = itens.stream()
                .map(VendaItem::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorDescontoVenda = itens.stream().map(VendaItem::getValorDesconto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorTotal = valorBruto.subtract(valorDescontoVenda);

        venda.setValorBruto(valorBruto);
        venda.setValorDesconto(valorDescontoVenda);
        venda.setValorTotal(valorTotal);

        List<VendaPagamento> pagamentos = montarPagamentos(dto.pagamentos(), venda);
        venda.setPagamentos(pagamentos);

        validarPagamentos(valorTotal, pagamentos);

        Venda salvo = vendaRepository.save(venda);

        return vendaMapper.toResponseDto(salvo);
    }

    public VendaResponseDto buscarPorId(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada"));

        return vendaMapper.toResponseDto(venda);
    }

    public List<VendaResponseDto> listar() {
        return vendaRepository.findAll()
                .stream()
                .map(vendaMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public VendaResponseDto  cancelarVenda(Long id, VendaCancelarRequestDto dto) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada"));

        if (venda.getStatus() == VendaStatus.CANCELADA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Venda já está cancelada");
        }

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

         //devolve estoque
        for(VendaItem item : venda.getItens()){
            Produto produto = item.getProduto();

            if(Boolean.TRUE.equals(produto.getControlaEstoque())){
                BigDecimal estoqueAtual = produto.getEstoqueAtual() != null ? produto.getEstoqueAtual() : BigDecimal.ZERO;
                produto.setEstoqueAtual(estoqueAtual.add(item.getQuantidade()));
                produtoRepository.save(produto);
            }
        }

         //cancela pagamentos
        for(VendaPagamento pagamento : venda.getPagamentos()){
            pagamento.setStatus(StatusPagamento.CANCELADO);
        }

        venda.setStatus(VendaStatus.CANCELADA);
        venda.setMotivoCancelamento(dto.motivo());
        venda.setUsuarioCancelamento(funcionario);
        venda.setDataHoraCancelamento(Instant.now());

        Venda salva = vendaRepository.save(venda);
        return vendaMapper.toResponseDto(salva);
    }

    public List<VendaResponseDto> listarVendasPorCaixa(Long caixaId) {
        return vendaRepository.findByCaixaId(caixaId)
                .stream()
                .map(vendaMapper::toResponseDto)
                .toList();
    }

    public List<VendaRegistrarRecebimentoResponseDto> listarVendasAbertasEmAtendimentoPorCliente(Long clienteId) {

        List<VendaStatus> statusPermitidos = List.of(VendaStatus.ABERTA, VendaStatus.EM_ATENDIMENTO, VendaStatus.PARCIAL);
        List<Venda> vendas = vendaRepository.findByClienteIdAndStatusIn(clienteId, statusPermitidos);

        return vendas.stream()
                .map(v -> new VendaRegistrarRecebimentoResponseDto(
                        v.getId(),
                        v.getDataHoraVenda(),
                        //v.getValorTotal()
                        calcularSaldoRestante(v)
                ))
                .toList();
    }

    public List<VendaResponseDto> listarVendasCompletoAbertasEEmAtendimento(Long clienteId) {
        List<VendaStatus> statusPermitidos = List.of(VendaStatus.ABERTA, VendaStatus.EM_ATENDIMENTO, VendaStatus.PARCIAL);
        List<Venda> vendas = vendaRepository.findByClienteIdAndStatusIn(clienteId, statusPermitidos);

        return vendas.stream()
                .map(venda -> {
                    VendaResponseDto dto = vendaMapper.toResponseDto(venda);

                    // ajusta valorTotal para vendas PARCIAIS
                    if (venda.getStatus() == VendaStatus.PARCIAL) {
                        BigDecimal totalPago = venda.getPagamentos().stream()
                                .map(VendaPagamento::getValor)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        // sobrescreve o valorTotal no DTO com o saldo restante
                        return new VendaResponseDto(
                                dto.id(),
                                dto.dataHoraVenda(),
                                dto.valorBruto(),
                                dto.valorDesconto(),
                                venda.getValorTotal().subtract(totalPago), // saldo restante
                                dto.status(),
                                dto.observacoes(),
                                dto.cliente(),
                                dto.caixa(),
                                dto.itens(),
                                dto.pagamentos()
                        );
                    }

                    return dto;
                })
                .toList();
    }

    private List<VendaItem> montarItens(List<VendaItemCreateRequestDto> itensDto, Venda venda) {
        if (itensDto == null || itensDto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A venda precisa ter pelo menos um item");
        }

        return itensDto.stream().map(dto -> {
            Produto produto = produtoRepository.findById(dto.produtoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

            Animal animal = null;
            if (dto.animalId() != null) {
                animal = animalRepository.findById(dto.animalId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado"));
            }

            Funcionario funcionario = null;
            if (dto.funcionarioId() != null) {
                funcionario = funcionarioRepository.findById(dto.funcionarioId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
            }

            BigDecimal valorDesconto = dto.valorDesconto() != null
                    ? dto.valorDesconto()
                    : BigDecimal.ZERO;

            BigDecimal valorTotal = dto.valorUnitario()
                    .multiply(dto.quantidade())
                    .subtract(valorDesconto);

            //BAIXA DO ESTOQUE
            if (Boolean.TRUE.equals(produto.getControlaEstoque())) {

                BigDecimal estoqueAtual = produto.getEstoqueAtual() != null
                        ? produto.getEstoqueAtual()
                        : BigDecimal.ZERO;

                produto.setEstoqueAtual(
                        estoqueAtual.subtract(dto.quantidade())
                );

                produtoRepository.save(produto);
            }

            VendaItem item = new VendaItem();
            item.setVenda(venda);
            item.setProduto(produto);
            item.setAnimal(animal);
            item.setFuncionario(funcionario);
            item.setDescricao(produto.getNome());
            item.setQuantidade(dto.quantidade());
            item.setValorUnitario(dto.valorUnitario());
            item.setValorDesconto(valorDesconto);
            item.setValorTotal(valorTotal);

            return item;
        }).toList();
    }

    @Transactional
    public List<VendaPagamentoHistoricoResponseDto> buscarHistoricoPagamentos(Long vendaId) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Venda não encontrada"
                ));

        return venda.getPagamentos().stream()
                .map(p -> new VendaPagamentoHistoricoResponseDto(
                        p.getValor(),
                        p.getVenda().getDataHoraVenda(),
                        p.getFormaRecebimento().getNome(),
                        p.getParcelas() != null
                                ? p.getParcelas().stream()
                                .map(parcela -> new VendaPagamentoParcelaHistoricoDto(
                                        parcela.getValor(),
                                        parcela.getDataPagamento() // ou createdAt da parcela
                                ))
                                .toList()
                                : List.of()
                ))
                .toList();
    }

    @Transactional()
    public VendaResponseDto buscarUltimaVendaPorCaixa(Long caixaId) {
        Venda venda = vendaRepository.findFirstByCaixaIdOrderByDataHoraVendaDesc(caixaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma venda encontrada para este caixa"));

        return vendaMapper.toResponseDto(venda);
    }

    private List<VendaPagamento> montarPagamentos(List<VendaPagamentoCreateRequestDto> pagamentosDto, Venda venda) {
        if (pagamentosDto == null || pagamentosDto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A venda precisa ter pelo menos um pagamento");
        }

        return pagamentosDto.stream().map(dto -> {
            FormaRecebimento forma = formaRecebimentoRepository.findById(dto.formaRecebimentoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de recebimento não encontrada"));

            VendaPagamento pagamento = new VendaPagamento();
            pagamento.setVenda(venda);
            pagamento.setFormaRecebimento(forma);
            pagamento.setValor(dto.valor());
            pagamento.setQuantidadeParcelas(dto.quantidadeParcelas());
            pagamento.setStatus(StatusPagamento.PAGO);
            pagamento.setDataPagamento(Instant.now());

            List<VendaPagamentoParcela> parcelas = gerarParcelas(pagamento);
            pagamento.setParcelas(parcelas);

            return pagamento;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional()
    public List<VendaListaPdvResponseDto> listarVendasPorPeriodo(String dataInicioStr, String dataFimStr) {
        LocalDate dataInicio = LocalDate.parse(dataInicioStr);
        LocalDate dataFim = LocalDate.parse(dataFimStr);

        Instant inicio = dataInicio.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant fim = dataFim.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Venda> vendas = vendaRepository.findByDataHoraVendaBetweenOrderByDataHoraVendaAsc(inicio, fim);

        return vendas.stream().map(v -> {
            List<String> nomesPets = v.getItens().stream()
                    .map(VendaItem::getAnimal)
                    .filter(a -> a != null)
                    .map(a -> a.getNome())
                    .collect(Collectors.toList());

            return new VendaListaPdvResponseDto(
                    v.getId(),
                    v.getCliente() != null ? v.getCliente().getNomeCompleto() : null,
                    nomesPets,
                    v.getValorTotal(),
                    v.getStatus()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<LocalizarVendaResponseDto> localizarVendas(LocalizarVendaRequestDto filtro) {

        Instant inicio = null;
        Instant fim = null;

        if (filtro.data() != null) {
            LocalDate dia = LocalDate.parse(filtro.data());
            inicio = dia.atStartOfDay(ZoneId.systemDefault()).toInstant();
            fim = dia.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        }

        List<Venda> vendas;

        if (filtro.codigo() != null) {
            vendas = vendaRepository.findById(filtro.codigo())
                    .map(List::of)
                    .orElse(List.of());
        } else {
            vendas = vendaRepository.filtrarVendas(
                    filtro.clienteId(),
                    filtro.status(),
                    inicio,
                    fim
            );
        }

        return vendas.stream().map(v -> {
            // calcular valorPago apenas se for PARCIAL
            var valorPago = v.getStatus() == VendaStatus.PARCIAL
                    ? v.getPagamentos().stream()
                    .map(VendaPagamento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    : v.getValorTotal();

            return new LocalizarVendaResponseDto(
                    v.getId(),
                    v.getDataHoraVenda().toString(),
                    v.getCliente() != null ? new LocalizarVendaResponseDto.ClienteResumoDto(
                            v.getCliente().getId(),
                            v.getCliente().getNomeCompleto()
                    ) : null,
                    v.getItens().stream()
                            .map(i -> i.getAnimal() != null
                                    ? new LocalizarVendaResponseDto.AnimalResumoDto(
                                    i.getAnimal().getId(),
                                    i.getAnimal().getNome()
                            )
                                    : null)
                            .filter(a -> a != null)
                            .toList(),
                    v.getValorTotal(),
                    valorPago,
                    v.getStatus()
            );
        }).toList();
    }

    private List<VendaPagamento> montarPagamentosFinalizar(
            List<VendasPagamentosFinalizeRequestDto> pagamentosDto,
            Venda venda
    ) {
        return pagamentosDto.stream()
                .map(dto -> {
                    FormaRecebimento forma = formaRecebimentoRepository.findById(dto.formaRecebimentoId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de recebimento não encontrada"));

                    VendaPagamento pagamento = new VendaPagamento();
                    pagamento.setVenda(venda);
                    pagamento.setFormaRecebimento(forma);
                    pagamento.setValor(dto.valor());
                    pagamento.setQuantidadeParcelas(dto.quantidadeParcelas());
                    pagamento.setStatus(StatusPagamento.PAGO);
                    pagamento.setDataPagamento(Instant.now());

                    List<VendaPagamentoParcela> parcelas = gerarParcelas(pagamento);
                    pagamento.setParcelas(parcelas);

                    return pagamento;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<VendaPagamentoParcela> gerarParcelas(VendaPagamento pagamento) {
        int quantidadeParcelas = pagamento.getQuantidadeParcelas() != null
                ? pagamento.getQuantidadeParcelas()
                : 1;

        if (quantidadeParcelas <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade de parcelas inválida");
        }

        BigDecimal valorParcela = pagamento.getValor()
                .divide(BigDecimal.valueOf(quantidadeParcelas), 2, RoundingMode.HALF_UP);

        List<VendaPagamentoParcela> parcelas = new ArrayList<>();

        for (int i = 1; i <= quantidadeParcelas; i++) {
            VendaPagamentoParcela parcela = new VendaPagamentoParcela();
            parcela.setPagamento(pagamento);
            parcela.setNumeroParcela(i);
            parcela.setValor(valorParcela);
            parcela.setDataVencimento(LocalDate.now().plusMonths(i - 1));
            parcela.setDataPagamento(Instant.now());
            parcela.setStatus(StatusPagamento.PAGO);

            parcelas.add(parcela);
        }

        return parcelas;
    }

    private void validarPagamentos(BigDecimal valorTotal, List<VendaPagamento> pagamentos) {
        BigDecimal totalPago = pagamentos.stream()
                .map(VendaPagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPago.compareTo(valorTotal) != 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Total dos pagamentos deve ser igual ao valor total da venda"
            );
        }
    }

    public List<VendaResponseDto> listarVendasPorCliente(Long clienteId, VendaStatus status) {
        List<Venda> vendas;

        if (status == null) {
            vendas = vendaRepository.findByClienteId(clienteId);
        } else {
            vendas = vendaRepository.findByClienteIdAndStatus(clienteId, status);
        }

        return vendas.stream()
                .map(vendaMapper::toResponseDto)
                .toList();
    }

    public BigDecimal calcularSaldoRestante(Venda venda) {
        BigDecimal totalPago = venda.getPagamentos().stream()
                .map(VendaPagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return venda.getValorTotal().subtract(totalPago);
    }


}
