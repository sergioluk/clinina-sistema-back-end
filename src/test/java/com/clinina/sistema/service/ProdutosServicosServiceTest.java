package com.clinina.sistema.service;

import com.clinina.sistema.mapper.ProdutoMapper;
import com.clinina.sistema.model.entity.Produto;
import com.clinina.sistema.repository.GrupoRepository;
import com.clinina.sistema.repository.MarcaRepository;
import com.clinina.sistema.repository.ProdutosServicosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProdutosServicosServiceTest {
    @InjectMocks
    private ProdutosServicosService service;

    @Mock
    private ProdutosServicosRepository repository;

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    /*@Test
    void deveCriarProdutoSemMarcaEGrupo() {

        ProdutoCreateRequestDto dto = new ProdutoCreateRequestDto(
                null,
                "123",
                "Produto Teste",
                "UN",
                null,
                null,
                null,
                BigDecimal.TEN,
                false,
                null,
                null,
                null,
                false,
                null,
                TipoControleDesconto.PERMITE_DESCONTO_ATE_100,
                null,
                true,
                true,
                true
        );

        Produto produto = new Produto();

        ProdutoCreateRequestDto retornoMock = new ProdutoCreateRequestDto(
                null,
                "123",
                "Produto Teste",
                "UN",
                null,
                null,
                null,
                BigDecimal.TEN,
                false,
                null,
                null,
                null,
                false,
                null,
                TipoControleDesconto.PERMITE_DESCONTO_ATE_100,
                null,
                true,
                true,
                true
        );

        when(produtoMapper.toEntity(dto)).thenReturn(produto);
        when(repository.save(produto)).thenReturn(produto);

        when(produtoMapper.toDtoCompleto(produto)).thenReturn(retornoMock);

        ProdutoCreateRequestDto resultado = service.criarProduto(dto);

        assertNotNull(resultado);
        assertEquals("123", resultado.codigoDeBarras());
        assertEquals("Produto Teste", resultado.nome());
        verify(repository).save(produto);
    }

    @Test
    void deveLancarErroQuandoMarcaNaoExiste() {

        ProdutoCreateRequestDto dto = new ProdutoCreateRequestDto(
                null,
                "123",
                "Produto Teste",
                "UN",
                1L,
                null,
                null,
                BigDecimal.TEN,
                false,
                null,
                null,
                null,
                false,
                null,
                TipoControleDesconto.PERMITE_DESCONTO_ATE_100,
                null,
                true,
                true,
                true
        );

        Produto produto = new Produto();

        when(produtoMapper.toEntity(dto)).thenReturn(produto);
        when(marcaRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.criarProduto(dto)
        );
    }

    @Test
    void deveAtualizarProduto() {
        ProdutoCreateRequestDto dto = new ProdutoCreateRequestDto(
                1L,
                "123",
                "Novo Nome",
                "UN",
                null,
                null,
                null,
                BigDecimal.TEN,
                false,
                null,
                null,
                null,
                false,
                null,
                TipoControleDesconto.PERMITE_DESCONTO_ATE_100,
                null,
                true,
                true,
                true
        );

        Long id = 1L;
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome("Antigo nome");

        when(repository.findById(id)).thenReturn(Optional.of(produto));
        when(repository.save(produto)).thenAnswer(i -> i.getArgument(0));
        when(produtoMapper.toDtoCompleto(produto)).thenReturn(dto);

        ProdutoCreateRequestDto resultado = service.atualizarProduto(dto);

        assertNotNull(resultado);
        assertEquals("Novo Nome", resultado.nome());

        verify(repository).save(produto);

    }*/

    @Test
    void deveBuscarProdutoPorId() {

        Produto produto = new Produto();
        produto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        service.buscarProdutoPorId(1L);

        verify(repository).findById(1L);
    }

}
