package service;

import java.util.ArrayList;

import dao.RatingDAO;
import model.Produto;
import model.Rating;
import model.Usuario;
import spark.Request;
import spark.Response;

public class RatingService {
	
	private RatingDAO ratingdao = new RatingDAO();
	
	public String Update(Request request, Response response, UsuarioService UServ) {
		String result = "";
		
		try {
			int prodid = Integer.parseInt(request.params(":prodid"));
			int rating = Integer.parseInt(request.queryParams("rating"));
			System.out.println(prodid + " " + rating);
			if(rating < 1 || rating > 5) {
				throw new Exception ("Rating fora do limite");
			}
			String token = request.headers("Authorization");
			Usuario user = UServ.UserToken(token);
			if(user == null) {
				throw new Exception("Usuario nao autenticado!");
			}
			Rating rate = new Rating(user.getUserId(), prodid, rating);
			ratingdao.update(rate);
			
			
		} catch (Exception e) {
			result = e.getMessage();
			response.status(404);
		}
		return result;
		
	}
	
	public String Inserir(Request request, Response response, UsuarioService UServ) {
		String result = "";
		
		try {
			int prodid = Integer.parseInt(request.params(":prodid"));
			String token = request.headers("Authorization");
			Usuario user = UServ.UserToken(token);
			if(user == null) {
				throw new Exception("Usuario nao autenticado!");
			}
			Rating rate = new Rating(user.getUserId(), prodid);
			ratingdao.insert(rate);
			
			
		} catch (Exception e) {
			result = e.getMessage();
			response.status(404);
		}
		return result;
	}
	
	public String Recomendar(Request request, Response response, UsuarioService UServ, ProdutoService PServ) {
		String result = "";
		try {
			String token = request.headers("Authorization");
			Usuario user = UServ.UserToken(token);
			if(user == null) {
				throw new Exception("Usuario nao autenticado!");
			}
			result = ratingdao.Recomendacao(user);
			
			
		} catch (Exception e) {
			result = e.getMessage();
			response.status(404);
		}
		return result;
	}
	
	public String getAll(Request request, Response response, UsuarioService UServ, ProdutoService PServ) {
			String result = "";
			try {
				String token = request.headers("Authorization");
				Usuario user = UServ.UserToken(token);
				if(user == null) {
					throw new Exception("Usuario nao autenticado!");
				}
				ArrayList<Rating> lista = ratingdao.getAll(user);
				result += "[";
				for(int i = 0; i < lista.size(); i++) {
					Rating index = lista.get(i);
					Produto prod = PServ.get(index.getProduto());
					if(i > 0) {
						result+=",";
					}
					result += "{"
							+ "\"usuario\": "+index.getUsuario()+","
							+ "\"produto\": "+index.getProduto()+","
							+ "\"nomeProd\": \""+prod.getNome()+"\","
							+ "\"rating\": "+index.getRating()
							+ "}";
					
					
				}
				result += "]";
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				result = e.getMessage();
				response.status(404);
			}
			return result;
	}
	
}
