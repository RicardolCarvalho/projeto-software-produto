package br.insper.produto.controller;

import br.insper.produto.Produto;
import br.insper.produto.ProdutoController;
import br.insper.produto.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTests {

	@InjectMocks
	private ProdutoController produtoController;

	@Mock
	private ProdutoService produtoService;

	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
	}

	@Test
	void test_CriarProduto() throws Exception {
		Produto produto = new Produto();
		produto.setNome("Produto Teste");
		produto.setPreco(10.0f);
		produto.setQuantidade(5);

		Mockito.when(produtoService.criarProduto(Mockito.any(Produto.class))).thenReturn(produto);

		ObjectMapper objectMapper = new ObjectMapper();

		mockMvc.perform(MockMvcRequestBuilders.post("/api/produto")
						.content(objectMapper.writeValueAsString(produto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Produto Teste"));
	}

	@Test
	void test_BuscarProdutoPorId() throws Exception {
		Produto produto = new Produto();
		produto.setId("123");
		produto.setNome("Produto Teste");

		Mockito.when(produtoService.buscarProdutoPorId("123")).thenReturn(produto);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/produto/123"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Produto Teste"));
	}

	@Test
	void test_ListarProdutos() throws Exception {
		List<Produto> produtos = Arrays.asList(
				new Produto(), new Produto()
		);

		Mockito.when(produtoService.listarProdutos()).thenReturn(produtos);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/produto"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
	}

	@Test
	void test_DiminuirQuantidade() throws Exception {
		Produto produto = new Produto();
		produto.setId("123");
		produto.setNome("Produto Teste");
		produto.setQuantidade(3);

		Mockito.when(produtoService.diminuirQuantidade("123", 2)).thenReturn(produto);

		mockMvc.perform(MockMvcRequestBuilders.put("/api/produto/123/menos/2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.quantidade").value(3));
	}
}
