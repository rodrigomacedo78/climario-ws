package br.com.climario.service.impl;

import java.util.List;

import org.exparity.hamcrest.BeanMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import br.com.climario.dominio.Cliente;
import br.com.climario.dominio.Pedido;
import br.com.climario.service.IPedidoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:climario-test-context.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class PedidoServiceImplTest {
	
	@Autowired
	IPedidoService pedidoService;
	
	@Test
	public void criarPedido() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste");
		c.setCodigo("07828359705");
		c = pedidoService.criarCliente(c);		
		
		Pedido pedido = new Pedido();
		pedido.setCliente(c);
		pedido.setNumero("3298432");
		
		pedido = pedidoService.criar(pedido);
		MatcherAssert.assertThat(pedido.getId(), Matchers.notNullValue());
		MatcherAssert.assertThat(pedido.getId(), Matchers.greaterThan(0l));
		MatcherAssert.assertThat(pedido.getNumero(), Matchers.is(Matchers.equalTo("3298432")));
		MatcherAssert.assertThat(pedido.getCliente().getId(), Matchers.notNullValue());
		MatcherAssert.assertThat(pedido.getCliente().getId(), Matchers.greaterThan(0l));
	}
	
	@Test
	public void existeCliente() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste 2");
		c.setCodigo("07828359708");
		c = pedidoService.criarCliente(c);		
		
		MatcherAssert.assertThat(c.getId(), Matchers.notNullValue());
		MatcherAssert.assertThat(c.getId(), Matchers.greaterThan(0l));
		MatcherAssert.assertThat(pedidoService.isClienteExiste(c.getCodigo()), Matchers.is(Matchers.equalTo(true)));
	}
	
	@Test
	public void recuperarCliente() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste 2");
		c.setCodigo("07828359709");
		c = pedidoService.criarCliente(c);
		
		MatcherAssert.assertThat(c.getId(), Matchers.notNullValue());
		MatcherAssert.assertThat(c.getId(), Matchers.greaterThan(0l));
		MatcherAssert.assertThat(pedidoService.isClienteExiste(c.getCodigo()), Matchers.is(Matchers.equalTo(true)));
		MatcherAssert.assertThat(pedidoService.recuperarCliente(c.getCodigo()), BeanMatchers.theSameAs(c));
	}
	
	@Test
	public void pedidosPorCliente() {
		
		Cliente c = new Cliente();
		c.setNome("Cliente de Teste Final");
		c.setCodigo("07828359711");
		c = pedidoService.criarCliente(c);
		

		Pedido pedido1 = new Pedido();
		pedido1.setCliente(c);
		pedido1.setNumero("3298433");
		pedidoService.criar(pedido1);
		
		Pedido pedido2 = new Pedido();
		pedido2.setCliente(c);
		pedido2.setNumero("3298434");
		pedidoService.criar(pedido2);
		
		Pedido pedido3 = new Pedido();
		pedido3.setCliente(c);
		pedido3.setNumero("3298435");
		pedidoService.criar(pedido3);
		
		List<Pedido> pedidos = pedidoService.listarPedidosPorCliente(c.getCodigo());
		
		MatcherAssert.assertThat(pedidos.size(), Matchers.greaterThan(2));
	}
	
	@Test(expected = RuntimeException.class)
	public void pedidosPorClienteCodigoEmpty() {
		
		pedidoService.listarPedidosPorCliente("");
	}
	
	@Test(expected = RuntimeException.class)
	public void pedidosPorClienteCodigoNull() {
		
		pedidoService.listarPedidosPorCliente(null);
	}
}
