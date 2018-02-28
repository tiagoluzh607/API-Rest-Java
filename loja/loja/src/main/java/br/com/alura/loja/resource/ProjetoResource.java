package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.ProjetoDAO;
import br.com.alura.loja.modelo.Projeto;

@Path("projetos")
public class ProjetoResource {
	
	@Path("{id}") // acessar com /id no final do link exemplo localhost:8080/projetos/1
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@PathParam("id") long id) { // acessar localhost:8080/projetos/1
		
		ProjetoDAO projetoDAO = new ProjetoDAO();
		Projeto projeto = projetoDAO.busca(id);
		return projeto.toXML();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response adiciona(String conteudo) { //mandar um projeto xml via post para localhost:8080/projetos
		
		Projeto projeto = (Projeto) new XStream().fromXML(conteudo);
		new ProjetoDAO().adiciona(projeto);
		
		URI uri = URI.create("/projetos/"+projeto.getId()); //cria uma uri com o local do novo recurso criado
		return Response.created(uri).build(); //retorna uma o código http de sucesso e manda a url onde se encontra o recurso, esse caminho cai no header da resposta Ex: [Location: http://localhost:8080/projetos/2]
	}
	
	@Path("{id}") // acessar /id no final da url exemplo localhost:8080/projetos/1
	@DELETE
	public Response remove(@PathParam("id") long id) {
		new ProjetoDAO().remove(id); // remove o projeto
		return Response.ok().build(); // retorna o status code 200 ok
	}

}
