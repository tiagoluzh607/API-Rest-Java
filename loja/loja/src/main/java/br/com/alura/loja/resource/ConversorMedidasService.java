package br.com.alura.loja.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("servicos")
public class ConversorMedidasService {

	
	
	@GET //Defini qual o verbo http que vai utilizado pra chamar esse método
	@Path("quilometrosParaMilhas/{quilometros}")//Defini qual vai ter o url pra acessar o método, sendo o {quilometros} um parâmetro que é quantidade de quilometro que vai ser convertido pra milhas
	public Response quilometroParaMilha(@PathParam("quilometros")Double quilometros){ 	//Metodo que faz um simples conversão de quilometro para milhas
			
		quilometros = quilometros / 1.6;
			return Response.ok(quilometros).build();
	}
	
	@GET
	@Path("milhasParaQuilometros/{milhas}")
	//Metodo que faz um simples conversão de milhas para quilometros
	public Response milhasParaQuilometros(@PathParam("milhas")Double milhas)
	{
			milhas = milhas * 1.6;

			return Response.ok(milhas).build();
	}

}
