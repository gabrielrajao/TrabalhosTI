//ProdutoService

package service;

import com.auth0.jwt.interfaces.DecodedJWT;

import auth.ESessionToken;
import service.EmpresaService;
import dao.EmpresaDAO;
import java.util.ArrayList;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.util.List;
import dao.ProdutoDAO;
import model.Empresa;
import model.Produto;
import spark.Request;
import spark.Response;


public class ProdutoService {

	private ProdutoDAO produtoDAO = new ProdutoDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_PRODID = 1;
	private final int FORM_ORDERBY_FUNCAO = 2;
	private final int FORM_ORDERBY_PRECO = 3;
	
	
	public String insert(Request request, Response response, EmpresaService empresaService) {
		Produto produto = new Produto();
	    produto.setFuncao(request.queryParams("funcao"));
	    produto.setPreco(Float.parseFloat(request.queryParams("preco")));
	    produto.setTipo(request.queryParams("tipo"));
	    produto.setNome(request.queryParams("nome"));
	    produto.setProdUrl(request.queryParams("produrl"));
	    String resultado = "";
	    
	    
	  //pegando o header authorization da request
	  	String token = request.headers("Authorization");
	  	System.out.println(token);
	    
	    Empresa empresa = empresaService.ValidaToken(token);
	    if(empresa == null) {
	    	System.out.println("Erro");
	    } else {
	    	produto.setCodEmpresa(empresa.getCompanyId());
	    	System.out.println(produto.getCodEmpresa());
	    }
	    
	    
	    
	    try {
	        if (produtoDAO.insert(produto)) {
	            response.status(201); // 201 Created
	            resultado = "Produto (" + produto.getFuncao() + ") inserido com sucesso!";
	        } else {
	            response.status(404); // 404 Not Found
	            resultado = "Falha ao inserir o produto (" + produto.getFuncao() + ").";
	        }
	    } catch (Exception e) {
	        response.status(500); // 500 Internal Server Error
	        resultado = "Erro interno do servidor: " + e.getMessage();
	    }

	    // Você precisa definir o retorno adequado aqui, dependendo de como seu aplicativo lida com as respostas.
	    return resultado;
	}
	
	public Produto get(int prodId) {
		Produto result = produtoDAO.get(prodId);
		return result;
		
	}
	
	public String getById(Request request, Response response) {
		int prodid = Integer.parseInt(request.params(":prodid"));
		Produto result = get(prodid);
		response.status(404);
		String res = "";
		if(result != null) {
			response.status(200);
			res = "{"
					+ "\"prodid\": "+result.getPRODID()+","
					+ "\"nome\": \""+result.getNome()+"\","
					+ "\"descricao\": \""+result.getFuncao()+"\","
					+ "\"preco\": "+result.getPreco()+","
					+ "\"produrl\": \""+result.getProdUrl() + "\""
					+ "}";
		}
		return res;
	}
	
	public String get(Request request, Response response, EmpresaService empresaService) {
		
	   ArrayList<Produto> produto = new ArrayList<Produto>(); 
	   String JsonFront = "";
		
	  //pegando o header authorization da request
	  	String token = request.headers("Authorization");
	  	System.out.println(token);
	    
	    Empresa empresa = empresaService.ValidaToken(token);
	    if(empresa == null) {
	    	System.out.println("Erro");
	    } else {
	    	int codEmpresa = empresa.getCompanyId();
	    	System.out.println(codEmpresa);
	    	produto = produtoDAO.getEmpresa(codEmpresa);
	    }
		    
	    
	    if(produto == null) {
	    	response.status(404);
	    } else {
	    	JsonFront+= "{ \"data\":[";
		    for(int i = 0; i < produto.size(); i++) {
		    	Produto index = produto.get(i);
		    	JsonFront += "{" 
		    			  + "\"prodid\": " + index.getPRODID() + ","
		    			  + "\"funcao\": \"" + index.getFuncao() + "\","
		    			  + "\"nome\": \"" + index.getNome() + "\","
		    			  + "\"preco\": " + index.getPreco() + ","
		    			  + "\"tipo\": \"" + index.getTipo() + "\""
		    			  + "}";
		    	if(i + 1 < produto.size()) {
		    		JsonFront += ",";
		    	}
		    }
		    JsonFront += "]}";
		    System.out.println(JsonFront);
		    response.status(201);
	    }
		return JsonFront;
	}
	
	public String delete(Request request, Response response) {
        int prodid = Integer.parseInt(request.params(":prodid"));
        Produto produto = produtoDAO.get(prodid);
        String resp = "";       

        if (produto != null) {
            produtoDAO.delete(prodid);
            response.status(200); // success
            resp = "Produto (" + prodid + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Produto (" + prodid + ") não encontrado!";
        }
        return resp;
	}
	
}