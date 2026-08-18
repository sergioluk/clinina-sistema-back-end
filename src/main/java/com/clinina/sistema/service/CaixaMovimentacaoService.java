package com.clinina.sistema.service;

import com.clinina.sistema.dto.caixa.request.CaixaDespesaCreateRequestDto;
import com.clinina.sistema.dto.caixa.request.CaixaMovimentacaoCreateRequestDto;
import com.clinina.sistema.dto.caixa.request.CaixaSangriaCreateRequestDto;
import com.clinina.sistema.dto.caixa.request.CaixaTransferenciaCreateRequestDto;
import com.clinina.sistema.model.entity.*;
import com.clinina.sistema.model.enums.TipoMovimentacao;
import com.clinina.sistema.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class CaixaMovimentacaoService {

    private final CaixaRepository caixaRepository;
    private final ContaRepository contaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FormaRecebimentoRepository formaRecebimentoRepository;
    private final CaixaMovimentacaoRepository caixaMovimentacaoRepository;
    private final DespesaCategoriaRepository despesaCategoriaRepository;

    public CaixaMovimentacaoService(
            CaixaRepository caixaRepository,
            ContaRepository contaRepository,
            FuncionarioRepository funcionarioRepository,
            FormaRecebimentoRepository formaRecebimentoRepository,
            CaixaMovimentacaoRepository caixaMovimentacaoRepository,
            DespesaCategoriaRepository despesaCategoriaRepository
    ) {
        this.caixaRepository = caixaRepository;
        this.contaRepository = contaRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.formaRecebimentoRepository = formaRecebimentoRepository;
        this.caixaMovimentacaoRepository = caixaMovimentacaoRepository;
        this.despesaCategoriaRepository = despesaCategoriaRepository;
    }

    @Transactional
    public void salvarSuprimento(CaixaMovimentacaoCreateRequestDto dto) {
        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        Caixa caixa = caixaRepository.findById(dto.caixaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));

        Conta contaOrigem = contaRepository.findById(dto.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de origem não encontrada"));

        FormaRecebimento forma = formaRecebimentoRepository.findById(dto.formaRecebimentoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de recebimento não encontrada"));

        // Criar movimentação do tipo SUPRIMENTO
        CaixaMovimentacao movimentacao = new CaixaMovimentacao();
        movimentacao.setCaixa(caixa);
        movimentacao.setUsuario(funcionario);
        movimentacao.setConta(contaOrigem);
        movimentacao.setFormaRecebimento(forma);
        movimentacao.setValor(dto.valor());
        movimentacao.setDescricao(dto.descricao());
        movimentacao.setTipo(TipoMovimentacao.SUPRIMENTO);
        movimentacao.setDataHora(Instant.now());

        caixaMovimentacaoRepository.save(movimentacao);

        // Debita a conta de origem
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(dto.valor()));

        contaRepository.save(contaOrigem);
        caixaRepository.save(caixa);
    }

    @Transactional
    public void salvarSangria(CaixaSangriaCreateRequestDto dto) {

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        Caixa caixa = caixaRepository.findById(dto.caixaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));

        Conta contaDestino = contaRepository.findById(dto.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta de destino não encontrada"));

        // Buscar forma de pagamento "Dinheiro"
        FormaRecebimento formaDinheiro = formaRecebimentoRepository.findByNome("Dinheiro")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de recebimento 'Dinheiro' não encontrada"));

        // Movimentação de saída (sangria)
        CaixaMovimentacao sangria = new CaixaMovimentacao();
        sangria.setCaixa(caixa);
        sangria.setUsuario(funcionario);
        sangria.setConta(contaDestino);
        sangria.setValor(dto.valor());
        sangria.setDescricao(dto.descricao());
        sangria.setTipo(TipoMovimentacao.SANGRIA);
        sangria.setFormaRecebimento(formaDinheiro);
        sangria.setDataHora(Instant.now());

        caixaMovimentacaoRepository.save(sangria);

        contaDestino.setSaldo(contaDestino.getSaldo().add(dto.valor()));

        contaRepository.save(contaDestino);
        caixaRepository.save(caixa);
    }

    @Transactional
    public void salvarDespesa(CaixaDespesaCreateRequestDto dto) {

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        Caixa caixa = caixaRepository.findById(dto.caixaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));

        FormaRecebimento forma = formaRecebimentoRepository.findById(dto.formaRecebimentoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Forma de recebimento não encontrada"));

        DespesaCategoria categoria = despesaCategoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria de despesa não encontrada"));

        // Movimentação da despesa
        CaixaMovimentacao despesa = new CaixaMovimentacao();
        despesa.setCaixa(caixa);
        despesa.setUsuario(funcionario);
        despesa.setFormaRecebimento(forma);
        despesa.setTipo(TipoMovimentacao.DESPESA);
        despesa.setDescricao(dto.descricao() + (dto.observacao() != null ? " (" + dto.observacao() + ")" : "")
                + " - Categoria: " + categoria.getNome());
        despesa.setConta(null); // Conta específica não necessária
        despesa.setValor(dto.valor());
        despesa.setDataHora(Instant.now());

        caixaMovimentacaoRepository.save(despesa);

        caixaRepository.save(caixa);
    }

    @Transactional
    public void salvarTransferencia(CaixaTransferenciaCreateRequestDto dto) {

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        Caixa caixaOrigem = caixaRepository.findById(dto.caixaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa de origem não encontrado"));

        Caixa caixaDestino = caixaRepository.findById(dto.caixaDestinoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa de destino não encontrado"));

        if (caixaOrigem.getId().equals(caixaDestino.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O caixa de origem e destino devem ser diferentes."
            );
        }

        FormaRecebimento formaDinheiro = formaRecebimentoRepository.findByNome("Dinheiro")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Forma de recebimento 'Dinheiro' não encontrada"));

        // movimentação de saída (caixa de origem)
        CaixaMovimentacao movSaida = new CaixaMovimentacao();
        movSaida.setCaixa(caixaOrigem);
        movSaida.setCaixaRelacionado(caixaDestino);
        movSaida.setUsuario(funcionario);
        movSaida.setTipo(TipoMovimentacao.TRANSFERENCIA);
        movSaida.setDescricao("Transferência para o caixa " + caixaDestino.getNome());
        movSaida.setValor(dto.valor());
        movSaida.setFormaRecebimento(formaDinheiro);
        movSaida.setDataHora(Instant.now());

        caixaMovimentacaoRepository.save(movSaida);

        //movimentação de entrada (conta de destino)
        CaixaMovimentacao movEntrada = new CaixaMovimentacao();
        movEntrada.setCaixa(caixaDestino);
        movEntrada.setCaixaRelacionado(caixaOrigem);
        movEntrada.setUsuario(funcionario);
        movEntrada.setTipo(TipoMovimentacao.TRANSFERENCIA);
        movEntrada.setDescricao("Transferência recebida do caixa " + caixaOrigem.getNome());
        movEntrada.setValor(dto.valor());
        movEntrada.setFormaRecebimento(formaDinheiro);
        movEntrada.setDataHora(Instant.now());

        caixaMovimentacaoRepository.save(movEntrada);

        caixaRepository.saveAll(List.of(caixaOrigem, caixaDestino));
    }
}
