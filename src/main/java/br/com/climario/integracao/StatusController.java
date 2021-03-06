package br.com.climario.integracao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.climario.ui.PedidoView;
import br.com.climario.ui.Util;
import br.com.climario.dominio.Pedido;
import br.com.climario.dominio.Pedido.PedidoStatus;
import br.com.climario.service.impl.PedidoServiceImpl;
import br.com.climario.service.impl.ServiceLocator;
import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.NotificationService;

@WebServlet(urlPatterns = "/status")
public class StatusController extends HttpServlet {
	
	private static final long serialVersionUID = 6656505569565364366L;
	
	public String envioEmail = "marcio@internit.com.br";
	
	//public String envioEmailU = "jonath@internit.com.br";
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.addHeader("Access-Control-Allow-Origin", "https://sandbox.pagseguro.uol.com.br");
		
		String texto = null;
		String texto2 = null;
		
		try {
			
			Pedido pedido = ServiceLocator.getInstance().getPedidoService().recuperarPedidoPorTransacao(request.getParameter("transaction_id"));
			
			System.out.println(request.getParameter("transaction_id"));
			Map<String, String> st = new HashMap<String, String>();
				st.put("4" , "APROVADA");
				st.put("7" , "PENDENTE");
				st.put("5" , "EXPIRADA");
				st.put("6" , "REJEITADA");
			
			PedidoStatus ps = PedidoStatus.getTIpo(st.get(request.getParameter("state_pol")));
			
			texto2 = "Prezado(a)  "+pedido.getCliente().getNome()+", <br /> ";
			
			if (ps.toString().equals("APROVADA"))
			{
				texto = "O cliente "+pedido.getCliente().getNome()+", completou com sucesso o processo de pagamento para o pedido "+pedido.getNumero()+" e est?? com o pagamento aprovado. Favor encaminhar a log??stica o n??mero do pedido, juntamente com este email informando sobre a aprova????o do pagamento.<br /><br />";
				
				texto2 += "Seu pedido "+pedido.getNumero()+" teve o pagamento aprovado pela sua operadora de cart??o. A partir deste instante o mesmo entrar?? em processo de separa????o, expedi????o e entrega.<br /><br />";
				texto2 += "Em caso de d??vida ou problema entre em contato com o consultor de vendas que lhe atendeu.<br /><br />";
				   
			}
			else if (ps.toString().equals("REJEITADA"))
			{
				texto = "O cliente "+pedido.getCliente().getNome()+", n??o teve sucesso com o pagamento para o pedido "+pedido.getNumero()+" e est?? com o pagamento recusado. Retornar o contato ao cliente.<br /><br />";
				
				texto2 += "Seu pedido "+pedido.getNumero()+" teve o pagamento reprovado pela sua operadora de cart??o.<br /><br />";
				texto2 += "Em caso de d??vidas sobre a reprova????o, favor entrar em contato com a sua operadora de cart??o.<br /><br />";
			}
			
				texto += "Clima Rio<br>";
				texto += "Sempre a melhor compra.<br/><br/>";
				texto += "<img src='http://climariopagamentos.com.br/javax.faces.resource/img/clima_logo.jpg.jsf?ln=media'>";
				
				
				texto2 += "Clima Rio<br/>";
				texto2 += "Sempre a melhor compra.<br/><br/>";
				texto2 += "<img src='http://climariopagamentos.com.br/javax.faces.resource/img/clima_logo.jpg.jsf?ln=media'>";
				
				
				String emailEnvio = "";
				
				if(pedido.getCliente().getEmailRca().isEmpty())
				{
					emailEnvio = envioEmail;
				}else{
					emailEnvio = pedido.getCliente().getEmailRca();
				}
			//ClimaRio
			Util.sendMail(emailEnvio, "Solicitar Pedido", texto);
			//Comprador
			Util.sendMail(pedido.getCliente().getEmail(), "Solicitar Pedido", texto2);
			
			//transaction_id
			ServiceLocator.getInstance().getPedidoService().atulizarStatus(request.getParameter("transaction_id"), ps);
		  
		} catch (Exception e) {  
		    System.err.println(e.getMessage());  
		}
	}

	


}
