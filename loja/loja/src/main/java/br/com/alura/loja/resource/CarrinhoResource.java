package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;

@Path("carrinhos")
public class CarrinhoResource {

	@Path("{id}") //indica a url como /id no final /carrinhos/id
	@GET
	@Produces(MediaType.APPLICATION_XML) // indica que estamos produzindo um media type xml
	public String busca(@PathParam("id") long id) { //acessar localhost:8080/carrinhos/1
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.toXML();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML) //indica que só aceitamos no http o media type xml
	public Response adiciona(String conteudo) { //mandar um carrinho xml via post para localhost:8080/carrinhos
		
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		new CarrinhoDAO().adiciona(carrinho);
		
	
		URI uri = URI.create("/carrinhos/" + carrinho.getId()); //cria uma uri com o local do novo recurso criado
		return Response.created(uri).build(); //retorna uma o código http de sucesso e manda a url onde se encontra o recurso, esse caminho cai no header da resposta [Location: http://localhost:8080/carrinhos/2]
	}
	
	@Path("{id}/produtos/{produtoId}")
	@DELETE
	public Response removeProduto(@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		carrinho.remove(produtoId);
		return Response.ok().build();
	}
}
