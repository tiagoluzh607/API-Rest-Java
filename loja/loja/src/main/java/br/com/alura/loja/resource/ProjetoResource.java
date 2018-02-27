package br.com.alura.loja.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.alura.loja.dao.ProjetoDAO;
import br.com.alura.loja.modelo.Projeto;

@Path("projetos")
public class ProjetoResource {
	
	@Path("{id}") // acessar com /id no final do link
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String busca(@PathParam("id") long id) { // acessar localhost:8080/projetos/1
		
		ProjetoDAO projetoDAO = new ProjetoDAO();
		Projeto projeto = projetoDAO.busca(id);
		return projeto.toXML();
	}

}
