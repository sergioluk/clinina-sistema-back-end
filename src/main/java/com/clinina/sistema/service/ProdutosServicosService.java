package com.clinina.sistema.service;

import com.clinina.sistema.dto.*;
import com.clinina.sistema.dto.produto.request.*;
import com.clinina.sistema.dto.produto.response.*;
import com.clinina.sistema.mapper.ProdutoMapper;
import com.clinina.sistema.model.entity.Grupo;
import com.clinina.sistema.model.entity.Marca;
import com.clinina.sistema.model.entity.Produto;
import com.clinina.sistema.model.enums.SituacaoEstoque;
import com.clinina.sistema.model.enums.StatusValidade;
import com.clinina.sistema.model.enums.TipoMensagem;
import com.clinina.sistema.model.enums.TipoProduto;
import com.clinina.sistema.repository.GrupoRepository;
import com.clinina.sistema.repository.MarcaRepository;
import com.clinina.sistema.repository.ProdutosServicosRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProdutosServicosService {

    private final ProdutosServicosRepository produtosServicosRepository;
    private final GrupoRepository grupoRepository;
    private final MarcaRepository marcaRepository;
    private final ProdutoMapper produtoMapper;

    private static final ZoneId ZONE_ID = ZoneId.of("America/Fortaleza");

    private static final Set<String> CAMPOS_ORDENACAO_PERMITIDOS = Set.of(
            "nome",
            "codigoDeBarras",
            "dataValidade",
            "estoqueAtual",
            "custo",
            "preco"
    );

    public ProdutosServicosService(ProdutosServicosRepository produtosServicosRepository, GrupoRepository grupoRepository, MarcaRepository marcaRepository, ProdutoMapper produtoMapper) {
        this.produtosServicosRepository = produtosServicosRepository;
        this.grupoRepository = grupoRepository;
        this.marcaRepository = marcaRepository;
        this.produtoMapper = produtoMapper;
    }

    public ProdutoPageResponseDto listarProdutos(int pagina, int tamanho, String busca, String ordenarPor, String direcao) {
        Sort sort = Sort.unsorted();

        if (ordenarPor != null && !ordenarPor.isBlank() && CAMPOS_ORDENACAO_PERMITIDOS.contains(ordenarPor)) {
            Sort.Direction direction = "desc".equalsIgnoreCase(direcao) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, ordenarPor);
        }

        Pageable pageable = PageRequest.of(pagina, tamanho, sort);
        Page<Produto> page;

        if(busca != null && !busca.isBlank()) {
            String termoNormalizado = normalize(busca);
            // 1 - Busca por código de barras
            Optional<Produto> produtoCodigo = produtosServicosRepository.findByCodigoDeBarras(busca);

            if(produtoCodigo.isPresent()) {
                page = new PageImpl<>(List.of(produtoCodigo.get()), pageable, 1);
            } else {
                // 2 - Busca FULLTEXT
                page = produtosServicosRepository.buscarPorNome(termoNormalizado, pageable);

                // 3 - fallback caso não encontre nada
                if(page.isEmpty()) {
                    List<Produto> produtos = produtosServicosRepository.findTop20ByNomeNormalizadoContaining(termoNormalizado);
                    page = new PageImpl<>(produtos, pageable, produtos.size());
                }
            }
        } else {
            page = produtosServicosRepository.findAll(pageable);
        }

        List<ProdutoResponseDto> produtos = page.getContent().stream().map(produto -> {
            SituacaoEstoque situacao = calcularSituacaoEstoque(produto);
            StatusValidade validade = calcularStatusVencimento(produto.getDataValidade());
            return produtoMapper.toDto(produto, calcularMarkup(produto), situacao, validade);
        }).toList();

        System.out.println("Total elementos: " + page.getTotalElements());
        System.out.println("Total paginas: " + page.getTotalPages());
        System.out.println("Conteudo: " + page.getContent().size());

        return new ProdutoPageResponseDto(produtos, page.getNumber(), page.getTotalPages(),
                page.getTotalElements(), page.getSize());
    }

    public ValidadeProdutosResponseDto buscarProdutosPorValidade() {

        LocalDate hoje = LocalDate.now(ZONE_ID);
        LocalDate limite = hoje.plusDays(60);

        List<Produto> vencidos = produtosServicosRepository.findByDataValidadeBefore(hoje);

        List<Produto> vencendo = produtosServicosRepository.findByDataValidadeBetween(hoje, limite);

        List<ValidadeProdutosNomesIdsResponseDto> produtosVencidos =
                vencidos.stream()
                        .map( p -> new ValidadeProdutosNomesIdsResponseDto(
                                p.getId(),
                                p.getNome(),
                                p.getDataValidade(),
                                p.getEstoqueAtual()
                        )).toList();

        List<ValidadeProdutosNomesIdsResponseDto> produtosVencendo =
                vencendo.stream()
                        .map(p -> new ValidadeProdutosNomesIdsResponseDto(
                                p.getId(),
                                p.getNome(),
                                p.getDataValidade(),
                                p.getEstoqueAtual()
                        )).toList();

        return new ValidadeProdutosResponseDto(produtosVencidos.size(), produtosVencidos, produtosVencendo.size(), produtosVencendo
        );
    }

    private StatusValidade calcularStatusVencimento(LocalDate dataValidade) {
        if (dataValidade == null) {
            return null;
        }
        if (dataValidade.isBefore(LocalDate.now(ZONE_ID))) {
            return StatusValidade.VENCIDO;
        }
        if (dataValidade.isBefore(LocalDate.now(ZONE_ID).plusDays(60))) {
            return StatusValidade.VENCENDO;
        }
        return StatusValidade.NORMAL;
    }

    private int calcularMarkup(Produto produto) {
        if (produto.getCusto() == null || produto.getPreco() == null || produto.getCusto().compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        return produto.getPreco().subtract(produto.getCusto())
                .divide(produto.getCusto(), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .intValue();
    }

    private SituacaoEstoque calcularSituacaoEstoque(Produto produto) {

        if (produto.getEstoqueAtual() == null) return null;

        if (produto.getEstoqueAtual().compareTo(BigDecimal.ZERO) == 0) return SituacaoEstoque.PARADO;

        // compara estoqueAtual <= estoqueMinimo
        if (produto.getEstoqueMinimo() != null &&
                produto.getEstoqueAtual().compareTo(produto.getEstoqueMinimo()) <= 0) {
            return SituacaoEstoque.REPOR;
        }

        // compara estoqueAtual > estoqueMaximo
        if (produto.getEstoqueMaximo() != null &&
                produto.getEstoqueAtual().compareTo(produto.getEstoqueMaximo()) > 0) {
            return SituacaoEstoque.EXCESSO;
        }

        return SituacaoEstoque.ADEQUADO;
    }

    public ProdutoCreateRequestDto criarProduto(ProdutoCreateRequestDto dto) {

        Marca marca = null;
        Grupo grupo = null;

        Produto produto = this.produtoMapper.toEntity(dto);

        if (dto.marcaId() != null) {
            marca = marcaRepository.findById(dto.marcaId())
                    .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
            produto.setMarca(marca);
        }

        if (dto.grupoId() != null) {
            grupo = grupoRepository.findById(dto.grupoId())
                    .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
            produto.setGrupo(grupo);
        }
        if (dto.tipo() == TipoProduto.SERVICO) {
            produto.setControlaEstoque(false);
            produto.setControlaValidade(false);
        }
        Produto salvo = this.produtosServicosRepository.save(produto);

        return this.produtoMapper.toDtoCompleto(salvo);
    }


    public ProdutoCreateRequestDto atualizarProduto(Long id, ProdutoCreateRequestDto dto) {
        Produto produto = this.produtosServicosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtoMapper.updateProdutoFromDto(dto, produto);

        Marca marca = null;
        Grupo grupo = null;

        if (dto.marcaId() != null) {
            marca = marcaRepository.findById(dto.marcaId())
                    .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
            produto.setMarca(marca);
        }

        if (dto.grupoId() != null) {
            grupo = grupoRepository.findById(dto.grupoId())
                    .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
            produto.setGrupo(grupo);
        }

        Produto salvo = produtosServicosRepository.save(produto);
        return this.produtoMapper.toDtoCompleto(salvo);
    }

    public ProdutoDetalhesResponseDto buscarProdutoPorId(Long id) {
        Produto produto = this.produtosServicosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        int tempoCadastro = getTempoCadastro(produto);
        SituacaoEstoque situacaoEstoque = calcularSituacaoEstoque(produto);
        StatusValidade statusValidade = calcularStatusVencimento(produto.getDataValidade());
        int markup = calcularMarkup(produto);
        int diasVencendo = 0;
        if (statusValidade != null && statusValidade.equals(StatusValidade.VENCENDO)) {
            diasVencendo = getDiasVencendo(produto.getDataValidade());
        }
        return this.produtoMapper.toDetalhesDto(produto, tempoCadastro, situacaoEstoque, statusValidade, markup, diasVencendo);
    }

    public boolean existeCodigoDeBarras(String codigoDeBarras) {
        return this.produtosServicosRepository.existsByCodigoDeBarras(codigoDeBarras);
    }

    private int getTempoCadastro(Produto produto) {
        if (produto.getCreatedAt() == null) {
            return 0;
        }
        return (int) java.time.Duration.between(produto.getCreatedAt(), Instant.now()).toDays();
    }

    private int getDiasVencendo(LocalDate dataValidade) {
        if (dataValidade == null) {
            return 0;
        }
        LocalDate hoje = LocalDate.now(ZONE_ID);
        if (dataValidade.isBefore(hoje)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(hoje, dataValidade);
    }

    public void atualizarDadosBasicos(Long produtoId, ProdutoDadosBasicosUpdateRequestDto dto) {

        Produto produto = this.produtosServicosRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setCodigoDeBarras(dto.codigoDeBarras());
        produto.setNome(dto.nome());
        produto.setUnidadeVenda(dto.unidadeVenda());
        produto.setTipo(dto.tipoProduto());

        produto.setBanhoTosa(dto.banhoTosa());
        produto.setClinica(dto.clinica());
        produto.setPetshop(dto.petshop());

        if (dto.marcaId() != null) {
            Marca marca = marcaRepository.findById(dto.marcaId())
                    .orElseThrow(() -> new RuntimeException("Marca não encontrada"));

            produto.setMarca(marca);
        } else {
            produto.setMarca(null);
        }

        if (dto.grupoId() != null) {
            Grupo grupo = grupoRepository.findById(dto.grupoId())
                    .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));

            produto.setGrupo(grupo);
        } else {
            produto.setGrupo(null);
        }

        this.produtosServicosRepository.save(produto);
    }

    public void atualizarCustoPreco(Long produtoId, ProdutoCustoPrecoUpdateRequestDto dto) {
        Produto produto = this.produtosServicosRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setProposito(dto.proposito());
        if (dto.custo() != null) {
            produto.setCusto(dto.custo());
        }
        produto.setMarkupDesejado(dto.markupDesejado());
        produto.setPreco(dto.preco());
        produto.setExibePreco(dto.exibePreco());
        produto.setPermiteAlterarPreco(dto.permiteAlterarPreco());

        this.produtosServicosRepository.save(produto);
    }

    public void atualizarValidade(Long produtoId, ProdutoValidadeUpdateRequestDto dto) {
        Produto produto = this.produtosServicosRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setControlaValidade(dto.controlaValidade());
        if (dto.dataValidade() != null) {
            produto.setDataValidade(dto.dataValidade());
        }

        this.produtosServicosRepository.save(produto);
    }

    public void atualizarLimiteDesconto(Long produtoId, ProdutoLimiteDescontoUpdateDto dto) {
        Produto produto = this.produtosServicosRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setTipoControleDesconto(dto.tipoControleDesconto());
        if (dto.descontoMaximo() != null) {
            produto.setDescontoMaximo(dto.descontoMaximo());
        }

        this.produtosServicosRepository.save(produto);
    }

    public void atualizarEstoque(Long produtoId, ProdutoEstoqueUpdateRequestDto dto) {
        Produto produto = this.produtosServicosRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setControlaEstoque(dto.controlaEstoque());
        produto.setEhFracionado(dto.ehFracionado());
        if (dto.minimo() != null) {
            produto.setEstoqueMinimo(dto.minimo());
        }
        if (dto.maximo() != null) {
            produto.setEstoqueMaximo(dto.maximo());
        }
        if (dto.estoque() != null) {
            produto.setEstoqueAtual(dto.estoque());
        }

        this.produtosServicosRepository.save(produto);
    }

    public List<ProdutoBuscaResponseDto> buscarProdutos(String termo) {

        String termoNormalizado = normalize(termo);

        Optional<Produto> produto = produtosServicosRepository.findByCodigoDeBarras(termo);

        List<Produto> produtos;

        if (produto.isPresent()) {
            produtos = List.of(produto.get());
        } else {
            produtos = produtosServicosRepository.buscarPorNome(termoNormalizado);

            if (produtos.isEmpty()) {
                produtos = produtosServicosRepository.findTop20ByNomeNormalizadoContaining(termoNormalizado);
            }
        }

        return produtos.stream()
                .map(p -> {
                    List<StatusMensagemDTO> mensagens = gerarMensagens(p);
                    return produtoMapper.toProdutoBuscaResponseDto(p, mensagens);
                })
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

    public ProdutoBuscaResponseDto buscarPorCodigoDeBarras(String codigoDeBarras) {
        Produto produto = produtosServicosRepository.findByCodigoDeBarras(codigoDeBarras)
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return produtoMapper.toProdutoBuscaResponseDto(produto, new ArrayList<StatusMensagemDTO>());
    }

    private List<StatusMensagemDTO> gerarMensagens(Produto produto) {
        List<StatusMensagemDTO> mensagens = new ArrayList<>();
        if (produto.getEstoqueAtual() != null) {
            BigDecimal estoqueAtual = produto.getEstoqueAtual();
            if (estoqueAtual.compareTo(BigDecimal.ZERO) == 0) {
                mensagens.add(new StatusMensagemDTO(TipoMensagem.ALERTA, "Sem estoque"));
            }
            if (estoqueAtual.compareTo(BigDecimal.ZERO) < 0) {//estoque < 0
                mensagens.add(new StatusMensagemDTO(TipoMensagem.ALERTA, estoqueAtual + " un em estoque"));
            }
            if (estoqueAtual.compareTo(BigDecimal.TEN) < 0) {//estoque < 10
                mensagens.add(new StatusMensagemDTO(TipoMensagem.INFO, estoqueAtual + " un em estoque"));
            }
        }
        if (produto.getDataValidade() != null) {
            StatusValidade statusValidade = calcularStatusVencimento(produto.getDataValidade());
            if (statusValidade == StatusValidade.VENCENDO) {
                mensagens.add(new StatusMensagemDTO(TipoMensagem.INFO, "Validade: " + formatarData(produto.getDataValidade())));
            } else if (statusValidade == StatusValidade.VENCIDO) {
                mensagens.add(new StatusMensagemDTO(TipoMensagem.ALERTA, "Vencido em " + formatarData(produto.getDataValidade())));
            }
        }

        return mensagens;
    }

    private String formatarData(LocalDate data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
