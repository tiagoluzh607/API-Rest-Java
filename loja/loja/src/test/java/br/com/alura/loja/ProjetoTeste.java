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
	private Client client;
	private WebTarget target;

	@Before
	public void startaServidor() {
		this.servidor = Servidor.inicializaServidor();
		this.client = ClientBuilder.newClient();
		this.target = this.client.target("http://localhost:8080");
	}
	
	@After
	public void stopServidor() {
		servidor.stop();
	}

	@Test
	public void testaConexaoDoResourceProjeto() {
		
		String conteudo = this.target.path("/projetos/1").request().get(String.class);
		
		Projeto projeto = (Projeto) new XStream().fromXML(conteudo);
		
		Assert.assertEquals("Minha loja", projeto.getNome());
	}
	
	@Test
	public void adicionaUmNovoProjeto() {
		 
		//criando projeto
		Projeto projeto = new Projeto(3, "Loja Tiaguera", 1996);
		
		String projetoXML = projeto.toXML();
		
		//Enviando via post
        Entity<String> entity = Entity.entity(projetoXML, MediaType.APPLICATION_XML); //monta string de envio
        
        Response response = this.target.path("/projetos").request().post(entity); // envia e pega o resultado
        Assert.assertEquals(201, response.getStatus()); // testa o resultado do Status code 201 (Criado com sucesso)
        
        String location = response.getHeaderString("Location"); //traz o conteúdo do cabeçalho Location(o server esta configurado para trazer o enderco do item criado)
        String conteudo = this.client.target(location).request().get(String.class); //faz uma requisicao get no novo endereço e pega o conteúdo
        Assert.assertTrue(conteudo.contains("Tiaguera")); // testa o conteudo para ver se vai vir o tablet criado a cima
	}
}
