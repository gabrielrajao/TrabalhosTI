// Essa classe gera tokens de autenticacao JWT(JSON Web Token) de LONGO PRAZO para empresas criarem suas contas

// Cada token dura uma semana e é de uso unico (Associado ao nome da empresa)


package auth;
import com.auth0.jwt.*;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;


public class ERegisterToken {
	
	//segredo do token (o algoritmo de codificacao se baseia nessa linha para codificar)
	private static final String secret = "t^yC/'5m*nSr}BkFyq5qH|}T@UY}]#b7g@xIZ(b<Oe2z9p/E)quv,]St*]4\"6+1";
	
	//tempo de expiracao do token (7 dias)
	private static final long EXPIRATION_TIME_MS = 604800000;
	
	//gerador de tokens
	public static String TokenGenerator(String CompanyName) {
		//data atual (de criacao)
		Date Criacao = new Date();
		//data de validade (data de criacao + tempo de expiracao)
		Date Validade = new Date(Criacao.getTime() + EXPIRATION_TIME_MS);
		
		//cria o algoritmo de codificacao em HMAC256, baseado na string segredo
		Algorithm assinatura = Algorithm.HMAC256(secret);
		
		//cria o token JWT com base no nome da empresa, data de validade/criacao e assinatura (algoritmo de codificacao)
		
		return JWT.create()
				.withSubject(CompanyName)
				.withIssuedAt(Criacao)
				.withExpiresAt(Validade)
				.sign(assinatura);
		//ao decodificar um token JWT, vc pode pegar o subject dele (no caso, o nome da empresa)
		
		//Os nomes serao unicos para cada empresa, isso que faz o token ser de uso unico.
		
		//a partir do momento que uma empresa tiver o nome do token, esse token nao podera ser mais usado
		//visto que daria erro de conflito (o backend devera checar se ja existe uma empresa com o nome do token)
	}
	
	//verifica validade e autenticidade do token, depois decodifica
	public static DecodedJWT verificaToken(String token) {
		//cria o algoritmo de codificacao em HMAC256, baseado na string segredo
		Algorithm assinatura = Algorithm.HMAC256(secret);
		
		//Construcao do verificador propriamente dito
		JWTVerifier verificador = JWT.require(assinatura).build();
		//Cria, mas nao inicializa o token decodificado (Tokens decodificados sao objetos de DecodedJWT)
		DecodedJWT result;
		//Tenta verificar o token com o verificador
		try {
			//Se tiver sucesso, o token decodificado sera salvo na variavel result criada anteriormente
			result = verificador.verify(token);
		} 
		//Excecao enviada quando o token e invalido
		catch(JWTVerificationException e) {
			//A funcao ira retornar um objeto null caso o token seja invalido
			result = null;
		}
		
		return result;
		
	}
	
}
