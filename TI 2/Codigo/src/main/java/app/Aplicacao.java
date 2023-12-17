package app;

import static spark.Spark.*;
import service.UsuarioService;
import service.PlantaService;
import service.AgendaService;
import service.EmpresaService;
import service.ProdutoService;
import service.RatingService;

public class Aplicacao {
	
	//inicizaliza plantaservice
	private static PlantaService plantService = new PlantaService();
	
	//inicializa objeto usuarioservice
	private static UsuarioService userService = new UsuarioService();
	
	//inicializa empresaservice
	private static EmpresaService empresaService =  new EmpresaService();
	
	//inicializa produtoService
	private static ProdutoService produtoService = new ProdutoService();
	
	//inicializa agendaService
	private static AgendaService agendaService = new AgendaService();
	
	//inicializa ratingService
	private static RatingService ratingService = new RatingService();
	
	//main
	public static void main(String[] args) {
		//escolhe a porta
		port(6789);
		
		//opcoes do CORS, essencial para fetch requests! essas opcoes definem o que pode ou nao ser enviado numa
		//requisicao
    	options("/*", (request,response)->{

    	    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
    	    if (accessControlRequestHeaders != null) {
    	        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
    	    }

    	    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
    	    if(accessControlRequestMethod != null){
    		response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
    	    }

    	    return "OK";
    	});  
		
    	//Parecido com o options, define a url que possui permissão para enviar requisições, tal como
    	//quais tipos de requisições esta pode enviar e quais headers ela pode ver
		before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.header("Access-Control-Expose-Headers", "Authorization");
        });
		
		
		
		//define a rota da funcao  LoginTOken
		//FUNÇÃO: Validar a autenticidade do token do usuario
		get("/usuario/token", (request,response) -> userService.LoginToken(request, response) );
		
		//define a rota da funcao  SenhaToken
		//FUNÇÃO: Validar a senha do usuario por meio do token de login
		get("/usuario/token/:senha", (request,response) -> userService.SenhaToken(request, response) );
		
		//define a rota da funcao  Insert
		//FUNÇÃO: Realiza cadastro de usuario
		post("/usuario/insert", (request,response) -> userService.Insert(request, response) );
		
		//define a rota da funcao  Login
		//FUNÇÃO: Realiza o login do usuario, assim como gera e envia o token de autenticacao
		get("/usuario/login", (request,response) -> userService.Login(request, response));
		
		//define a rota da funcao  Atualizar
		//FUNÇÃO: Atualiza as informacoes do usuario no banco de dados
		put("/usuario/update", (request,response) -> userService.Atualizar(request, response));
		
		
		//define a rota da funcao Deletar
		//FUNÇÃO: Deleta as informacoes do usuario no banco de dados
		delete("/usuario/delete", (request,response) -> userService.Deletar(request, response));
		
		
		
		//define a rota da funcao  LoginTOken
		//FUNÇÃO: Validar a autenticidade do token da empresa
		get("/empresa/token", (request,response) -> empresaService.LoginToken(request, response) );
				
		//define a rota da funcao  EmpresaToken
		//FUNÇÃO: Validar a senha da empresa por meio do token de login
		get("/empresa/token/:senha", (request,response) -> empresaService.SenhaToken(request, response) );
		
		//define a rota da funcao  Insert
		//FUNÇÃO: Realiza cadastro da empresa
		post("/empresa/insert", (request,response) -> empresaService.Insert(request, response) );
				
		//define a rota da funcao  Login
		//FUNÇÃO: Realiza o login da empresa, assim como gera e envia o token de autenticacao
		get("/empresa/login", (request,response) -> empresaService.Login(request, response));
			
		//define a rota da funcao  Atualizar
		//FUNÇÃO: Atualiza as informacoes da empresa no banco de dados
		put("/empresa/update", (request,response) -> empresaService.Atualizar(request, response));
			
		//define a rota da funcao Deletar
		//FUNÇÃO: Deleta as informacoes da empresa no banco de dados
		delete("/empresa/delete", (request,response) -> empresaService.Deletar(request, response));
		
		//define a rota da funcao  Insert
		//FUNÇÃO: Realiza cadastro de produtos
		post("/produto/insert", (request, response) -> produtoService.insert(request, response, empresaService));
		
		//define a rota da funcao  Get
		//FUNÇÃO: Realiza get de produtos
		get("/produto/get", (request, response) -> produtoService.get(request, response, empresaService));
		
		//getprod por id
		get("/produto/get/:prodid", (request,response) -> produtoService.getById(request, response));
		
		//define a rota da funcao Delete
		//FUNÇÃO: Realiza delete de produtos
		delete("/produto/delete/:prodid", (request, response) -> produtoService.delete(request, response));
		
		//Planta GETS
		get("/planta/get", (request,response) -> plantService.getAll(request, response) );
		
		//get search name
		get("/planta/get/:name", (request,response) -> plantService.searchName(request, response) );
		
		//get search results IA
		get("/planta/searchres",(request,response)-> plantService.searchResultados(request, response));
		
		//Agenda insert
		post("/agenda/insert/:planta", (request,response) -> agendaService.Inserir(request, response, userService));
		
		//Pega Calendario
		get("/agenda/calendario", (request,response) -> agendaService.getCalendario(request, response, userService, plantService));
		
		//deleteCalendario
        delete("/agenda/delete/:planta", (request, response) -> agendaService.delete(request, response, userService));
        
        //Recomendaprodutos
        get("/rating/recomendar", (request,response) -> ratingService.Recomendar(request, response, userService, produtoService));
        
        //Insere rating
        post("/rating/insert/:prodid", (request,response) -> ratingService.Inserir(request, response, userService));

        //GetAll ratings
        get("/rating/", (request,response) -> ratingService.getAll(request, response, userService, produtoService));
        
        //Atualiza rating
        put("/rating/update/:prodid", (request,response)-> ratingService.Update(request, response, userService));
		
	}
    
}
