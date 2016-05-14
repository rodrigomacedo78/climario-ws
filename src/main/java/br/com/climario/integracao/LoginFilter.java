package br.com.climario.integracao;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.climario.ui.LoginSession;

public class LoginFilter implements Filter {

	public static final String BEAN_NAME = "loginSession";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean isLoginPage = httpRequest.getRequestURI().contains("entrar.jsf");
        System.out.println("Acessando a " + httpRequest.getRequestURI());
        System.out.println("Login: " + isLoginPage);
        
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println(context);
        //LoginSession bean = context.getApplication().evaluateExpressionGet(context, "#{loginSession}", LoginSession.class);

        System.out.println(httpRequest.getSession().getAttribute(BEAN_NAME));
        LoginSession session = (LoginSession) httpRequest.getSession().getAttribute(BEAN_NAME);
        if(!isLoginPage && session == null) {
                
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/entrar.jsf");
        }
        /*else if(isLoginPage && session != null && !session.isLogged()){
        	
        	httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/entrar.jsf");
    	}*/
        else if(isLoginPage && session != null && session.isLogged()) 
        {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/index.jsf");
        }
        else {
                
                chain.doFilter(request, response);
        }
    }
    
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("Inicilizando filtro de login.");
    }

    public void destroy() {
    	System.out.println("Finalizando filtro de login.");
    }


	
}
