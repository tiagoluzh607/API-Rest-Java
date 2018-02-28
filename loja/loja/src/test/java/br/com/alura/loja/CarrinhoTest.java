package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

public class CarrinhoTest {
	
	private HttpServer server;
	private Client client;
	private WebTarget target;

	@Before
    public void before() {
        this.server = Servidor.inicializaServidor();
		this.client = ClientBuilder.newClient();
		this.target = this.client.target("http://localhost:8080");
    }

    @After
    public void mataServidor() {
        server.stop();
    }
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		
		String conteudo = this.target.path("/carrinhos/1").request().get(String.class);
		
		//Deserializar um carrinho em um objeto carrinho
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		
		//Verifica a rua do objeto carrinho
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
		
	}
	
	@Test
	public void testaAdicionarUmNovoCarrinho() {
		
		//criando carrinho
		Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        
        String carrinhoXML = carrinho.toXML();
        
        //Enviando via post
        Entity<String> entity = Entity.entity(carrinhoXML, MediaType.APPLICATION_XML); //monta string de envio
        
        Response response = this.target.path("/carrinhos").request().post(entity); // envia e pega o resultado
        Assert.assertEquals(201, response.getStatus()); // testa para ver se o código de retorno é 201 do http (criado com sucesso)
        
        //O servidor manda de volta no cabeçalho um location : que é a localização do carrinho criado, testamos se foi realmente criado
        
        String location = response.getHeaderString("Location"); //traz o conteúdo do cabeçalho Location(o server esta configurado para trazer o enderco do item criado)
        String conteudo = this.client.target(location).request().get(String.class); //faz uma requisicao get no novo endereço e pega o conteúdo
        Assert.assertTrue(conteudo.contains("Tablet")); // testa o conteudo para ver se vai vir o tablet criado a cima
        
        
	}

}
