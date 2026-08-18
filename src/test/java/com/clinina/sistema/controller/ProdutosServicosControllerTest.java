package com.clinina.sistema.controller;

import com.clinina.sistema.service.ProdutosServicosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(ProdutosServicosController.class)
class ProdutosServicosControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutosServicosService service;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    /*@Test
    void deveListarProdutos() throws Exception {

        ProdutoResponseDto dto = new ProdutoResponseDto(
                1L,
                "Produto 1",
                "123",
                null,
                10,
                BigDecimal.TEN,
                50,
                BigDecimal.valueOf(15),
                SituacaoEstoque.ADEQUADO
        );

        when(service.listarTodosProdutos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/produtos-servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Produto 1"))
                .andExpect(jsonPath("$[0].markup").value(50));
    }*/

    /*@Test
    void deveCriarProduto() throws Exception {

        ProdutoCreateRequestDto dto = new ProdutoCreateRequestDto(
                null,
                "123",
                "Produto Teste",
                "UN",
                null,
                null,
                BigDecimal.TEN,
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

        when(service.criarProduto(any())).thenReturn(dto);

        mockMvc.perform(post("/produtos-servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Produto Teste"));

        verify(service).criarProduto(any());
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {

        ProdutoCreateRequestDto dto = new ProdutoCreateRequestDto(
                1L,
                "123",
                "Produto",
                "UN",
                null,
                null,
                BigDecimal.TEN,
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

        when(service.buscarProdutoPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/produtos-servicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto"));
    }*/
}
