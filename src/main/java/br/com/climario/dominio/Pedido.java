package br.com.climario.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({ 
    @NamedQuery(name = "Pedido.por.cliente", query = "select p from Pedido p where p.cliente.codigo = :codigo")
})
public class Pedido {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(unique = true)
	private String numero;
	
	/*private String filial;
	
	private Date criacao;*/
	
	@ManyToOne
	@NotNull
	private Cliente cliente;
	
	/*private String descricaoCobranca;
	
	private String descricaoPlanoPagamento;
	
	@ElementCollection(fetch=FetchType.LAZY)
    @NotEmpty()
    @Size(min=1)
	private List<ItemPedido> itens = new ArrayList<>();
	
	private Float valorFrete;*/
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
