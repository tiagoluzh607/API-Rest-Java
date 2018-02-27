package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Projeto;
import junit.framework.Assert;

public class ProjetoTeste {
	
	private HttpServer servidor;

	@Before
	public void startaServidor() {
		this.servidor = Servidor.inicializaServidor();
	}
	
	@After
	public void stopServidor() {
		servidor.stop();
	}

	@Test
	public void testaConexaoDoResourceProjeto() {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		
		Projeto projeto = (Projeto) new XStream().fromXML(conteudo);
		
		Assert.assertEquals("Minha loja", projeto.getNome());
	}
	
	@Test
	public void adicionaUmNovoProjeto() {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		
		//criando projeto
		Projeto projeto = new Projeto(3, "Loja Tiaguera", 1996);
		
		String projetoXML = projeto.toXML();
		
		//Enviando via post
        Entity<String> entity = Entity.entity(projetoXML, MediaType.APPLICATION_XML); //monta string de envio
        
        Response response = target.path("/projetos").request().post(entity); // envia e pega o resultado
        Assert.assertEquals("<status>sucesso</status>", response.readEntity(String.class));
		
	}
}
