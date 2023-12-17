package service;

import com.auth0.jwt.interfaces.DecodedJWT;

import auth.ERegisterToken;
import auth.ESessionToken;
import dao.EmpresaDAO;
import model.Empresa;
import spark.Request;
import spark.Response;

public class EmpresaService {

	private EmpresaDAO empresadao = new EmpresaDAO();
	
	//funcao de cadastro
	public String Insert (Request request, Response response) {
		Empresa user = new Empresa();
		//pega as informacoes recebidas por queryParams (aquelas coisas depois da interrogacao na url)
		// e salva no objeto user criado na linha 17
		user.setEmail(request.queryParams("email"));
		user.setSenha(request.queryParams("senha"));
		DecodedJWT token = ERegisterToken.verificaToken(request.queryParams("login"));
		user.setLogin(token.getSubject());
		System.out.println("Token: " + user.getLogin());
		
		String resultado = "";
		try {
			//se o email nao estiver disponivel
			if(empresadao.emailAvaliable(user.getEmail()) == false) {
				//checa se login tambem esta disponivel
				if(empresadao.loginAvaliable(user.getLogin()) == false) {
					//envia uma excecao dizendo que ambos estao indisponiveis
					throw new IllegalArgumentException("ERRO: E/L JA ESTA SENDO UTILIZADO");
				}
				//envia uma excecao dizendo que o email esta indisponivel
				throw new IllegalArgumentException("ERRO: EMAIL JA ESTA SENDO UTILIZADO");
			}
			//checa se login esta disponivel
			if(empresadao.loginAvaliable(user.getLogin()) == false) {
				//envia uma excecao dizendo que o login esta indisponivel
				throw new IllegalArgumentException("ERRO: LOGIN JA ESTA SENDO UTILIZADO");
			}
			
			//funcao insert do user dao
			if(empresadao.insert(user) == true) {
				//se der certo envia codigo de status ok (entre 200 e 299)
				response.status(201);
				//body da resposta de sucesso
				resultado = "Empresa criada com sucesso!";
			} else {
				//se der algum erro desconhecido, enviar excecao
				throw new Exception("Erro ao criar Empresa ");
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
	
	//funcao para validar senha digitada pelo empresa com o token de autenticacao 
	public String SenhaToken (Request request, Response response) {
		String res = "";
		try {
			//pega a empresa da funcao user token (mais abaixo nessa classe)
			Empresa user = EmpresaToken(request);
			//se o objeto for null, enviar excecao relatando que o token e invalido
			if(user == null) {
				throw new SecurityException("ERRO: Token Inválido/expirado");
			}
			// Aqui criamos o objeto empresa de teste
			Empresa checarSenha = new Empresa();
			//definimos a senha como a senha digitada pela empresa, foi enviada como params (aquela parada com :)
			checarSenha.setSenha(request.params(":senha"));   
			//seta o login como o login do user recebido pelo token
			checarSenha.setLogin(user.getLogin());
			//ativa a funcao auth da EmpresaDAO, se der null, considera senha digitada pela empresa como invalida
			if(empresadao.Auth(checarSenha) == null) {
				throw new Exception("ERRO: Senha inválida");
			} else {
				//caso a funcao auth result em um objeto empresario retorna sucesso
				response.status(201);
				//o body da resposta e um json com as informacoes do empresa para ser usada na funcao de atualizar
				//empresa
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
		if(EmpresaToken(request) == null) {
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
	public Empresa EmpresaToken (Request request) {
		Empresa result = null;
		
		//pegando o header authorization da request
		String token = request.headers("Authorization");
		//System.out.println(token);
		//separa o token propriamente dito do "Bearer "
		token = (token.split(" "))[1];
		
		//decodifica token (Checar classe USessionToken no package Auth)
		DecodedJWT tokendecod = ESessionToken.verificaToken(token);
		try {
			//se o token retornado for null(valha na autenticacao)
			if(tokendecod == null) {
				//envia excecao de token invalido
				throw new Exception("Token inválido/expirado");
			} else {
				//se o token for validado com sucesso pega o userID dentro do subject do token decodificado
				int UserID = Integer.parseInt(tokendecod.getSubject());
				//retorna a empresa encontrado 
				result = empresadao.get(UserID);
			}
		} catch(Exception e) {
			//em caso de excecao, retorna null
			result = null;
		}
		return result;
	}
	
	
	//funcao de login de empresa
	public String Login (Request request, Response response) {
		String resultado = "";
		Empresa user = new Empresa();
		Empresa result;
		//define o login e a senha do objeto empresa recem criado como as strings login e senha recebidas
		//por query params
		user.setLogin(request.queryParams("login"));
		user.setSenha(request.queryParams("senha"));
		try {
			//testa autenticacao do empresa por meio da funcao Auth do EmpresaDAO
			if ( (result = empresadao.Auth(user)) != null) {
				//caso seja um sucesso retorna 201 (OK)
				System.out.println("EOMJP");
				response.status(201);
				//body da resposta
				resultado = "Login realizado com sucesso";
				//gera o token com base no userId e junta ele ao "Bearer "
				String m = "Bearer " + ESessionToken.TokenGenerator(""+ result.getCompanyId());
				//tipo da resposta
				response.type("text/plain");
				//envia o header authorization com token para o front end
				response.header("Authorization",  m);
			} else {
				
				//caso o empresa nao seja autenticado envia execao
				throw new Exception("ERRO: Login/Email ou Senha incorretos");
			}
		} catch (Exception e) {
			//qualquer excecao resultara em 404 (NOT FOUND) e o body da response igual a mensagem da excecao
			response.status(404);
			resultado = e.getMessage();
		}
		return resultado;
	}
	
	
	//funcao de deletar empresario
	public String Deletar (Request request, Response response) {
		String resultado = "";
		try {
			//essa parte e parecida com a de inserir
			Empresa user = EmpresaToken(request);
			if(user == null) {
				throw new SecurityException("ERRO: Token Inválido/expirado");
			}
			// Aqui checaremos se a senha digitada pela empresa e igual a senha armazenada no BD
			Empresa checarSenha = new Empresa();
			checarSenha.setSenha(request.queryParams("senha"));  
			
			checarSenha.setLogin(user.getLogin());
			if(empresadao.Auth(checarSenha) == null) {
				throw new Exception("ERRO: Senha inválida");
			} 
			//FIM DA PARTE PARECIDA COM INSERIR
			
			//Chama a funcao delete do EmpresaDAO
			if(empresadao.delete(user.getCompanyId()) == false) {
				//caso der erro, lancar excecao
				throw new Exception("Erro ao deletar a empresa");
			} else {
				//caso de sucesso, mensagens de sucesso
				response.status(201);
				resultado = "Empresa deletado com sucesso";
				//invalidando token da empresa
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
			Empresa user = EmpresaToken(request);
			if(user == null) {
				throw new SecurityException("ERRO: Token Inválido/expirado");
			}
			// Aqui checaremos se a senha digitada pelo empresa e igual a senha armazenada no BD
			Empresa checarSenha = new Empresa();
			checarSenha.setSenha( request.queryParams("senha"));   
			checarSenha.setLogin(user.getLogin());
			if(empresadao.Auth(checarSenha) == null) {
				throw new Exception("ERRO: Senha inválida");
			} 
			//FIM DA PARTE PARECIDA COM DELETE E INSERIR
			
			//Aqui as alteracoes sao feitas
			else {
				
				//pega a senha nova do queryParams
				user.setSenha(request.queryParams("novasenha"));
				
				//Se o email da empresa digitado for diferente do ja salvo no BD
				if(user.getEmail().equals(request.queryParams("email")) == false) {
					//define o novo email da empresa
					user.setEmail(request.queryParams("email"));
					//testa se o email esta disponivel
					if(empresadao.emailAvaliable(user.getEmail()) == false) {
						throw new Exception("ERRO: Email nao disponivel");
					}
				}
				
				//Checa se o login digitado pelo empresa e diferente do login ja salvo no BD
				if(user.getLogin().equals(request.queryParams("login")) == false) {
					//se for diferente, define o novo login da empresa
					user.setLogin(request.queryParams("login"));
					//checa se o empresa esta disponivel
					if(empresadao.loginAvaliable(user.getLogin() ) == false) {
						throw new Exception("ERRO: Login nao disponivel");
					}
				}
				//chama a funcao update do EmpresaDAO
				if(empresadao.update(user) == false) {
					//caso de erro
					throw new Exception("Erro ao atualizar empresa");
				} else {
					
					//mensagens de sucesso
					response.status(201);
					resultado = "Empresa atualizado com sucesso";
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
	
	
	public Empresa ValidaToken ( String token) {
		Empresa result = null;
		
		//System.out.println(token);
		//separa o token propriamente dito do "Bearer "
		token = (token.split(" "))[1];
		
		//decodifica token (Checar classe USessionToken no package Auth)
		DecodedJWT tokendecod = ESessionToken.verificaToken(token);
		try {
			//se o token retornado for null(valha na autenticacao)
			if(tokendecod == null) {
				//envia excecao de token invalido
				throw new Exception("Token inválido/expirado");
			} else {
				//se o token for validado com sucesso pega o userID dentro do subject do token decodificado
				int UserID = Integer.parseInt(tokendecod.getSubject());
				//retorna a empresa encontrado 
				result = empresadao.get(UserID);
			}
		} catch(Exception e) {
			//em caso de excecao, retorna null
			result = null;
		}
		return result;
	}
}
