package dao;

import model.Produto;
import model.Rating;
import model.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;



public class RatingDAO extends DAO {
	
	public RatingDAO() {
		super();
		conectar();
	}
	
	public void finalize() {
		close();
	}
	
	public boolean insert(Rating rate) {
		String sql = "INSERT INTO rating (usuario,produto,rating)"
				+ "VALUES (?,?,?)";
		boolean status = false;
		try {
			
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, rate.getUsuario());
			st.setInt(2, rate.getProduto());
			st.setInt(3, rate.getRating());
			st.executeUpdate();
			st.close();
			status = true;
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return status;
	}
	
	public boolean update(Rating rate) {
		String sql = "UPDATE rating SET rating = ? "
				+ "WHERE produto = ? AND usuario = ?";
		boolean status = false;
		try {
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(3, rate.getUsuario());
			st.setInt(2, rate.getProduto());
			st.setInt(1, rate.getRating());
			st.executeUpdate();
			st.close();
			status = true;
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return status;
	}
	
	public ArrayList<Rating> getUnrated(Usuario user) {
		ArrayList<Rating> result = null;
		String sql = "SELECT * FROM rating WHERE usuario = ?";
		try {
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, user.getUserId());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				int rating = rs.getInt("rating");
				if(rating == -1) {
					if(result == null) {
						result = new ArrayList<Rating>();
					}
					result.add(new Rating(user.getUserId(), rs.getInt("produto"), rating));
				}
				while(rs.next()) {
					rating = rs.getInt("rating");
					if(rating == -1) {
						if(result == null) {
							result = new ArrayList<Rating>();
						}
						result.add(new Rating(user.getUserId(), rs.getInt("produto"), rating));
					}
				}
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	
	public ArrayList<Rating> getRated(Usuario user) {
		ArrayList<Rating> result = null;
		String sql = "SELECT * FROM rating WHERE usuario = ?";
		try {
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, user.getUserId());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				
				int rating = rs.getInt("rating");
				if(rating != -1) {
					if(result == null) {
						result = new ArrayList<Rating>();
					}
					result.add(new Rating(user.getUserId(), rs.getInt("produto"), rating));
				}
				while(rs.next()) {
					rating = rs.getInt("rating");
					if(rating != -1) {
						if(result == null) {
							result = new ArrayList<Rating>();
						}
						result.add(new Rating(user.getUserId(), rs.getInt("produto"), rating));
					}
				}
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	public ArrayList<Rating> getAll(Usuario user) {
		ArrayList<Rating> result = null;
		String sql = "SELECT * FROM rating WHERE usuario = ?";
		try {
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, user.getUserId());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				result = new ArrayList<Rating>();
				int rating = rs.getInt("rating");
				result.add(new Rating(user.getUserId(), rs.getInt("produto"), rating));
				while(rs.next()) {
					rating = rs.getInt("rating");
						result.add(new Rating(user.getUserId(), rs.getInt("produto"), rating));
				}

			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	private String geraJSON (Usuario user) {
		String result = "";
		ArrayList<Rating> avaliados = getRated(user);
		if(avaliados != null) {
			result = "{ \"Inputs\": {"
					+ "\"input1\": [";
			
			for(int i = 0; i < avaliados.size(); i++) {
				Rating index = avaliados.get(i);
				if(i > 0) {
					result += ",";
				}
				result += "{"+
			            "\"userId\": "+(index.getUsuario() + 1000)+","+
			            "\"productId\": "+index.getProduto()+" ,"+
			            "\"rating\": "+index.getRating()+""+
			          "}";
			}
			
			result += "]"+
				      "},"+
				      "\"GlobalParameters\": {}"+
				    "}";
		}
		return result;
	}
	
	public String Recomendacao(Usuario user) {
			String result = "ERRO";
			String body = geraJSON(user);
			if(body.length() <= 0) {
				body = "{"+
			            "\"Inputs\": {"+
			        "\"input1\": ["+
			          "{"+
			            "\"userId\": 1,"+
			            "\"productId\": 29,"+
			            "\"rating\": 4"+
			          "},"+
			          "{"+
			            "\"userId\": 1,"+
			            "\"productId\": 94,"+
			            "\"rating\": 1"+
			          "},"+
			          "{"+
			            "\"userId\": 1,"+
			            "\"productId\": 43,"+
			            "\"rating\": 5"+
			          "},"+
			          "{"+
			            "\"userId\": 1,"+
			            "\"productId\": 36,"+
			            "\"rating\": 2"+
			          "},"+
			          "{"+
			            "\"userId\": 1,"+
			            "\"productId\": 24,"+
			            "\"rating\": 3"+
			          "}"+
			        "]"+
			      "},"+
			      "\"GlobalParameters\": {}"+
			    "}";
			}
			try {
		   //Chave da API	
		   String API_KEY = "fcw9SPFluLvJXIjILM22l6fbcqpxKsi1";
		   HttpRequest request = HttpRequest.newBuilder()
		         .uri(URI.create("http://81023499-6db4-42b9-b0b0-5bafc8e2392e.brazilsouth.azurecontainer.io/score"))
		         .headers("Content-Type", "application/json", "Authorization", "Bearer " + API_KEY)
		         .POST(BodyPublishers.ofString(body))
		         .build();
		   HttpClient client = HttpClient.newBuilder()
				   .build();
		   HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		   result = response.body();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	       return  result;
	}
	
}


