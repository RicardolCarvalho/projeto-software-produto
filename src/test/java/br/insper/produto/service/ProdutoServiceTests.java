package br.insper.produto.service;

import br.insper.produto.Produto;
import br.insper.produto.ProdutoRepository;
import br.insper.produto.ProdutoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTests {

	@InjectMocks
	private ProdutoService produtoService;

	@Mock
	private ProdutoRepository produtoRepository;

	@Test
	void test_CriarProduto() {
		Produto produto = new Produto();
		produto.setNome("Teste");
		produto.setPreco(20.0f);
		produto.setQuantidade(10);

		Mockito.when(produtoRepository.save(produto)).thenReturn(produto);

		Produto produtoCriado = produtoService.criarProduto(produto);

		Assertions.assertEquals("Teste", produtoCriado.getNome());
	}

	@Test
	void test_BuscarProdutoPorId() {
		Produto produto = new Produto();
		produto.setId("123");
		produto.setNome("Teste");

		Mockito.when(produtoRepository.findById("123")).thenReturn(Optional.of(produto));

		Produto produtoEncontrado = produtoService.buscarProdutoPorId("123");

		Assertions.assertEquals("Teste", produtoEncontrado.getNome());
	}

	@Test
	void test_BuscarProdutoPorIdNaoExistente() {
		Mockito.when(produtoRepository.findById("999")).thenReturn(Optional.empty());

		ResponseStatusException exception = Assertions.assertThrows(
				ResponseStatusException.class,
				() -> produtoService.buscarProdutoPorId("999")
		);

		Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
	}

	@Test
	void test_DiminuirQuantidade_Sucesso() {
		Produto produto = new Produto();
		produto.setId("123");
		produto.setNome("Teste");
		produto.setQuantidade(10);

		Mockito.when(produtoRepository.findById("123")).thenReturn(Optional.of(produto));
		Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);

		Produto produtoAtualizado = produtoService.diminuirQuantidade("123", 5);

		Assertions.assertEquals(5, produtoAtualizado.getQuantidade());
	}

	@Test
	void test_DiminuirQuantidade_Erro_EstoqueInsuficiente() {
		Produto produto = new Produto();
		produto.setId("123");
		produto.setNome("Teste");
		produto.setQuantidade(2);

		Mockito.when(produtoRepository.findById("123")).thenReturn(Optional.of(produto));

		IllegalArgumentException exception = Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> produtoService.diminuirQuantidade("123", 5)
		);

		Assertions.assertEquals("Estoque insuficiente.", exception.getMessage());
	}
}
