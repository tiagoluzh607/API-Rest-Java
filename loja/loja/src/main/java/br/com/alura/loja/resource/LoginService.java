package br.com.alura.loja.resource;

import java.util.Calendar;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.alura.loja.modelo.Credencial;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/login")
public class LoginService {

	private static final String FRASE_SEGREDO = "segredo";

	//Método POST que valida as credencias enviadas na request 
	//e se for validas retorna o token para o cliente	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fazerLogin(String credenciaisJson){
		
		try {
			Gson gson = new Gson();//Instancia o objeto Gson que vai ser responsável de transformar o corpo da request que está na variável crendenciaisJson em um objeto java Credencial
			Credencial credencial = gson.fromJson(credenciaisJson, Credencial.class); //aqui o objeto gson transforma a crendenciaisJson pra a variavel credencial do tipo Credencial
			validarCrendenciais(credencial); //Verifica se a credencial é valida, se não for vai dar exception 

			String token = gerarToken(credencial.getLogin(),2); //Se a credencial gera o token e passa a quantidade de dias que o token vai ser valido nesse caso 1 dia			
			return Response.ok(token).build(); //Retorna uma resposta com o status 200 OK com o token gerado

		} catch (Exception e) {

		e.printStackTrace();

			//Caso ocorra algum erro retorna uma resposta com o status 401 UNAUTHORIZED
			return Response.status(Status.UNAUTHORIZED).build();

		}

	}
	
	private void validarCrendenciais(Credencial crendencial) throws Exception {
	
		try {	
			if(!crendencial.getLogin().equals("teste") || !crendencial.getSenha().equals("123"))
					throw new Exception("Crendencias não válidas!");
		
		} catch (Exception e) {
				throw e;
		}
	}
	
	private String gerarToken(String login,Integer expiraEmDias ){

		//Defini qual vai ser o algoritmo da assinatura no caso vai ser o HMAC SHA512
		SignatureAlgorithm algoritimoAssinatura = SignatureAlgorithm.HS512;

		//Data atual que data que o token foi gerado
		Date agora = new Date();

		//Define até que data o token é pelo quantidade de dias que foi passo pelo parâmetro expiraEmDias
		Calendar expira = Calendar.getInstance();
		expira.add(Calendar.DAY_OF_MONTH, expiraEmDias);

		//Encoda a frase segredo pra base64 pra ser usada na geração do token 
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(FRASE_SEGREDO);

		SecretKeySpec key = new SecretKeySpec(apiKeySecretBytes, algoritimoAssinatura.getJcaName());

		//E finalmente utiliza o JWT builder pra gerar o token
		JwtBuilder construtor = Jwts.builder()

			.setIssuedAt(agora)//Data que o token foi gerado
			.setIssuer(login)//Coloca o login do usuário mais podia qualquer outra informação
			.signWith(algoritimoAssinatura, key)//coloca o algoritmo de assinatura e frase segredo já encodada
			.setExpiration(expira.getTime());// coloca até que data que o token é valido

			return construtor.compact();//Constrói o token retornando ele como uma String

		}

	public Claims validaToken(String token) {
		
		try{
		   //JJWT vai validar o token caso o token não seja valido ele vai executar uma exeption
		   //o JJWT usa a frase segredo pra descodificar o token e ficando assim possivel
		   //recuperar as informações que colocamos no payload
		   Claims claims = Jwts.parser()     

			     .setSigningKey(DatatypeConverter.parseBase64Binary(FRASE_SEGREDO))

				.parseClaimsJws(token).getBody();

				 //Aqui é um exemplo que se o token for valido e descodificado 
				 //vai imprimir o login que foi colocamos no token
				 System.out.println(claims.getIssuer());
		   return claims;

		}catch(Exception ex){
				throw ex;
		}
	}
}
