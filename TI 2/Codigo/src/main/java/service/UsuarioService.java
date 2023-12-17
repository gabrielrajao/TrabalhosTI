package service;

import com.auth0.jwt.interfaces.DecodedJWT;

import auth.USessionToken;
import dao.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;

public class UsuarioService {

	private UsuarioDAO userdao = new UsuarioDAO();
	
	//funcao de cadastro
	public String Insert (Request request, Response response) {
		Usuario user = new Usuario();
		//pega as informacoes recebidas por queryParams (aquelas coisas depois da interrogacao na url)
		// e salva no objeto user criado na linha 17
		user.setEmail(request.queryParams("email"));
		user.setSenha(request.queryParams("senha"));
		user.setLogin(request.queryParams("login"));
		
		String resultado = "";
		try {
			//se o email nao estiver disponivel
			if(userdao.emailAvaliable(user.getEmail()) == false) {
				//checa se login tambem esta disponivel
				if(userdao.loginAvaliable(user.getLogin()) == false) {
					//envia uma excecao dizendo que ambos estao indisponiveis
					throw new IllegalArgumentException("ERRO: E/L JA ESTA SENDO UTILIZADO");
				}
				//envia uma excecao dizendo que o email esta indisponivel
				throw new IllegalArgumentException("ERRO: EMAIL JA ESTA SENDO UTILIZADO");
			}
			//checa se login esta disponivel
			if(userdao.loginAvaliable(user.getLogin()) == false) {
				//envia uma excecao dizendo que o login esta indisponivel
				throw new IllegalArgumentException("ERRO: LOGIN JA ESTA SENDO UTILIZADO");
			}
			
			//funcao insert do user dao
			if(userdao.insert(user) == true) {
				//se der certo envia codigo de status ok (entre 200 e 299)
				response.status(201);
				//body da resposta de sucesso
				resultado = "Usuario criado com sucesso!";
			} else {
				//se der algum erro desconhecido, enviar excecao
				throw new Exception("Erro ao criar usuario ");
			}
		} 
		//ao encontrar excecao de conflito
		catch(IllegalArgumentException e) {
			//enviar codigo de confilto
			response.status(409);
			//body da resposta vira mensagem da excecao
			resultado = e.getMessage();
		}
		//ao encontrar excecao desconhecida
		catch(Exception e) {
			//codigo de erro interno do servidor
			response.status(500);
			//body da resposta vira mensagem da excecao
			resultado = e.getMessage();
		}
		
		return resultado;
	}
	
	//funcao para validar senha digitada pelo usuario com o token de autenticacao 
	public String SenhaToken (Request request, Response response) {
		

		String res = "";
		try {
			//pega o usuario da funcao user token (mais abaixo nessa classe)
			Usuario user = UserToken(request);
			//se o objeto for null, enviar excecao relatando que o token e invalido
			if(user == null) {
				throw new SecurityException("ERRO: Token Inválido/expirado");
			}
			// Aqui criamos o objeto usuario de teste
			Usuario checarSenha = new Usuario();
			//definimos a senha como a senha digitada pelo usuario, foi enviada como params (aquela parada com :)
			checarSenha.setSenha(request.params(":senha"));   
			//seta o login como o login do user recebido pelo token
			checarSenha.setLogin(user.getLogin());
			//ativa a funcao auth da UsuarioDAO, se der null, considera senha digitada pelo usuario como invalida
			if(userdao.Auth(checarSenha) == null) {
				throw new Exception("ERRO: Senha inválida");
			} else {
				//caso a funcao auth result em um objeto usuario retorna sucesso
				response.status(201);
				//o body da resposta e um json com as informacoes do usuario para ser usada na funcao de atualizar
				//usuario
				response.type("text/JSON");
				res = "{ \"login\" : \"" + user.getLogin() +"\","
								+ "\"email\" : \"" + user.getEmail()
 						+ "\"}";
			}
		}
		//caso token seja invalido
		catch(SecurityException e) {
			//mensagem da execao e o body da response
			res = e.getMessage();
			//status da response de nao autorizado
			response.status(401);
			//enviar header com token de autorizacao null
			response.header("Authorization", "Bearer NULL");
		} 
		
		catch (Exception e) {
			//erro ao digitar a senha ou qualquer outra execao, status 404 (NOT FOUND)
			res = e.getMessage();
			response.status(404);
		}
		
		
		return res;
	}
	
	//Funcao de autenticacao de tokens
	public String LoginToken (Request request, Response response) {

		String res = "";
		//Se a funcao UserToken der falha na autenticacao (funcao abaixo)
		if(UserToken(request) == null) {
			//codigo 401 (nao autorizado)
			response.status(401);
			//body da response
			res = "Token invalido/expirado";
			//envia header de autorizcao null 
			response.header("Authorization", "Bearer NULL");
		} else {
			//codigo representa ok
			response.status(201);
			//body da response
			res = "Token validado com sucesso!";
		}
		return res;
	}
	
	//funcao pega o header authorization de uma request e valida se o token nele e valido
	public Usuario UserToken (Request request) {
		Usuario result = null;
		
		//pegando o header authorization da request
		String token = request.headers("Authorization");
		
		//separa o token propriamente dito do "Bearer "
		token = (token.split(" "))[1];
		
		//decodifica token (Checar classe USessionToken no package Auth)
		DecodedJWT tokendecod = USessionToken.verificaToken(token);
		try {
			//se o token retornado for null(valha na autenticacao)
			if(tokendecod == null) {
				//envia excecao de token invalido
				throw new Exception("Token inválido/expirado");
			} else {
				//se o token for validado com sucesso pega o userID dentro do subject do token decodificado
				int UserID = Integer.parseInt(tokendecod.getSubject());
				//retorna o usuario encontrado 
				result = userdao.get(UserID);
			}
		} catch(Exception e) {
			//em caso de excecao, retorna null
			result = null;
		}
		return result;
	}
	
	public Usuario UserToken (String token) {
		Usuario result = null;
		
		
		//separa o token propriamente dito do "Bearer "
		token = (token.split(" "))[1];
		
		//decodifica token (Checar classe USessionToken no package Auth)
		DecodedJWT tokendecod = USessionToken.verificaToken(token);
		try {
			//se o token retornado for null(valha na autenticacao)
			if(tokendecod == null) {
				//envia excecao de token invalido
				throw new Exception("Token inválido/expirado");
			} else {
				//se o token for validado com sucesso pega o userID dentro do subject do token decodificado
				int UserID = Integer.parseInt(tokendecod.getSubject());
				//retorna o usuario encontrado 
				result = userdao.get(UserID);
			}
		} catch(Exception e) {
			//em caso de excecao, retorna null
			result = null;
		}
		return result;
	}
	
	
	
	//funcao de login de usuario
	public String Login (Request request, Response response) {
		String resultado = "";
		Usuario user = new Usuario();
		Usuario result;
		//define o login e a senha do objeto usuario recem criado como as strings login e senha recebidas
		//por query params
		user.setLogin(request.queryParams("login"));
		user.setSenha(request.queryParams("senha"));
		try {
			//testa autenticacao do usuario por meio da funcao Auth do UsuarioDAO
			if ( (result = userdao.Auth(user)) != null) {
				//caso seja um sucesso retorna 201 (OK)
				response.status(201);
				//body da resposta
				resultado = "Login realizado com sucesso";
				//gera o token com base no userId e junta ele ao "Bearer "
				String m = "Bearer " + USessionToken.TokenGenerator(""+ result.getUserId());
				//tipo da resposta
				response.type("text/plain");
				//envia o header authorization com token para o front end
				response.header("Authorization",  m);
			} else {
				//caso o usuario nao seja autenticado envia execao
				throw new Exception("ERRO: Login/Email ou Senha incorretos");
			}
		} catch (Exception e) {
			//qualquer excecao resultara em 404 (NOT FOUND) e o body da response igual a mensagem da excecao
			response.status(404);
			resultado = e.getMessage();
		}
		return resultado;
	}
	
	
	//funcao de deletar usuario
	public String Deletar (Request request, Response response) {
		String resultado = "";
		try {
			//essa parte e parecida com a de inserir
			Usuario user = UserToken(request);
			if(user == null) {
				throw new SecurityException("ERRO: Token Inválido/expirado");
			}
			// Aqui checaremos se a senha digitada pelo usuario e igual a senha armazenada no BD
			Usuario checarSenha = new Usuario();
			checarSenha.setSenha(request.queryParams("senha"));  
			
			checarSenha.setLogin(user.getLogin());
			if(userdao.Auth(checarSenha) == null) {
				throw new Exception("ERRO: Senha inválida");
			} 
			//FIM DA PARTE PARECIDA COM INSERIR
			
			//Chama a funcao delete do UsuarioDAO
			if(userdao.delete(user.getUserId()) == false) {
				//caso der erro, lancar excecao
				throw new Exception("Erro ao deletar o usuario");
			} else {
				//caso de sucesso, mensagens de sucesso
				response.status(201);
				resultado = "Usuario deletado com sucesso";
				//invalidando token de usuario
				response.header("Authorization", "Bearer NULL");
			}
			
		//parecida com a de inserir
		}catch(SecurityException e) {
			resultado = e.getMessage();
			response.status(401);
			response.header("Authorization", "Bearer NULL");
		} 
		
		catch (Exception e) {
			resultado = e.getMessage();
			response.status(404);
		}
		return resultado;
	}
	
	public String Atualizar(Request request, Response response) {
		String resultado = "";
		try {
			//parte parecida com delete e inserir
			Usuario user = UserToken(request);
			if(user == null) {
				throw new SecurityException("ERRO: Token Inválido/expirado");
			}
			// Aqui checaremos se a senha digitada pelo usuario e igual a senha armazenada no BD
			Usuario checarSenha = new Usuario();
			checarSenha.setSenha( request.queryParams("senha"));   
			checarSenha.setLogin(user.getLogin());
			if(userdao.Auth(checarSenha) == null) {
				throw new Exception("ERRO: Senha inválida");
			} 
			//FIM DA PARTE PARECIDA COM DELETE E INSERIR
			
			//Aqui as alteracoes sao feitas
			else {
				
				//pega a senha nova do queryParams
				user.setSenha(request.queryParams("novasenha"));
				
				//Se o email do usuario digitado for diferente do ja salvo no BD
				if(user.getEmail().equals(request.queryParams("email")) == false) {
					//define o novo email de usuario
					user.setEmail(request.queryParams("email"));
					//testa se o email esta disponivel
					if(userdao.emailAvaliable(user.getEmail()) == false) {
						throw new Exception("ERRO: Email nao disponivel");
					}
				}
				
				//Checa se o login digitado pelo usuario e diferente do login ja salvo no BD
				if(user.getLogin().equals(request.queryParams("login")) == false) {
					//se for diferente, define o novo login de usuario
					user.setLogin(request.queryParams("login"));
					//checa se o usuario esta disponivel
					if(userdao.loginAvaliable(user.getLogin() ) == false) {
						throw new Exception("ERRO: Login nao disponivel");
					}
				}
				//chama a funcao update do UsuarioDAO
				if(userdao.update(user) == false) {
					//caso de erro
					throw new Exception("Erro ao atualizar usuario");
				} else {
					
					//mensagens de sucesso
					response.status(201);
					resultado = "Usuario atualizado com sucesso";
				}
			}
		}
		//erro no token
		catch(SecurityException e) {
			resultado = e.getMessage();
			response.status(401);
			response.header("Authorization", "Bearer NULL");
		} 
		//erros diversos
		catch (Exception e) {
			resultado = e.getMessage();
			response.status(404);
		}
		return resultado;
	}
	
}
