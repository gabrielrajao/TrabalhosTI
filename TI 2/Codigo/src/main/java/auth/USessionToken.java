//   Essa classe gera tokens de autenticacao JWT(JSON Web Token) de CURTO-PRAZO para usuarios comuns

//   Tempo de validade dos tokens: 60 minutos
package auth;

import com.auth0.jwt.*;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

public class USessionToken {
	//segredo do token (o algoritmo de codificacao se baseia nessa linha para codificar)
	private static final String secret = "X+C<gv'-R8oT;/X].hV@7C?gh4-h#%PF:IKy!Brcy$w}XqKXNwWCE}2b@RZB/(0";
	
	//tempo de expiracao do token (1 hora)
	private static final long EXPIRATION_TIME_MS = 3600000;
	
	public static String TokenGenerator(String userId) {
		Date Criacao = new Date();
		Date Validade = new Date(Criacao.getTime() + EXPIRATION_TIME_MS);
		
		Algorithm assinatura = Algorithm.HMAC256(secret);
		//gera o token com base no userID (mais informacoes olhar no ERegisterToken.java)
		return JWT.create()
				.withSubject(userId)
				.withIssuedAt(Criacao)
				.withExpiresAt(Validade)
				.sign(assinatura);
	}
	
	//verifica e decodifica o token (mais informacoes olhar no ERegisterToken.java)
	public static DecodedJWT verificaToken(String token) {
		Algorithm assinatura = Algorithm.HMAC256(secret);
		JWTVerifier verificador = JWT.require(assinatura).build();
		DecodedJWT result;
		try {
			result = verificador.verify(token);
		} catch(JWTVerificationException e) {
			result = null;
		}
		
		return result;
		
	}
	
}
