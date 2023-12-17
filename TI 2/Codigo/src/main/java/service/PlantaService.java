package service;

import java.util.ArrayList;

import dao.PlantaDAO;
import spark.Request;
import spark.Response;
import model.Planta;

public class PlantaService {
	
	private PlantaDAO plantadao = new PlantaDAO();
	
	
	public Planta searchSlug(String slug) {
		return plantadao.searchSlug(slug);
	}
	
	public Planta getPlanta(int id) {
		return plantadao.getPlanta(id);
	}
	
	public String getAll(Request request, Response response) {
		String result = "";
		ArrayList<Planta> lista = plantadao.getAll();
		String plantas = "";
		int paginas = (int) Math.ceil(lista.size()/20);
		String paginaStr = (request.queryParams("page"));
		int pagina = 0;
		if(paginaStr != null && !paginaStr.isEmpty()) {
			try {
				pagina = Integer.parseInt(paginaStr);
			} catch (NumberFormatException e) {
				pagina = 0;
			}
		}
		try {
			if(paginas < pagina) {
				throw new IllegalArgumentException("Erro: Pagina nao existe");
			}
			plantas += "[";
			for(int i = pagina*20; i < 20*(pagina+1) && i < lista.size(); i++) {
				Planta index = lista.get(i);
				plantas += "{"
						+ "\"plantId\": \"" + index.getPlantId()   +"\","
						+ "\"slug\": \"" + index.getSlug()   +"\","
						+ "\"Cresc_forma\": \"" + index.getCresc_forma()   +"\","
						+ "\"Cresc_taxa\": \"" + index.getCresc_taxa()   +"\","
						+ "\"image_url\": \"" + index.getImagemurl()   +"\","
						+ "\"luz_solar\": " + index.getLuz_solar()   +","
						+ "\"nomecien\": \"" + index.getNomecien()   +"\","
						+ "\"nomepop\": \"" + index.getNome_pop()   +"\","
						+ "\"ph_max\": " + index.getPh_max()   +","
						+ "\"ph_min\": " + index.getPh_min()   +","
						+ "\"solo_nutrients\": " + index.getSolo_nutrientes()   +","
						+ "\"solo_salinidade\": " + index.getSolo_salinidade()   +","
						+ "\"solo_textura\": " + index.getSolo_textura()   +","
						+ "\"solo_umidade\": " + index.getSolo_umidade()   
						+ "}";
				if(i + 1 < 20*(pagina+1)  && i + 1 < lista.size() ) {
					plantas +=",";
				}
				
			}
			plantas+="]";
			String prau = "{";
			if(pagina > 0) {
				prau += "\"last\":" + (pagina-1) + ","; 
			}
			prau += "\"current\":" + (pagina) + ",";
			if(pagina < paginas ) {
				prau += "\"next\": " + (pagina +1) + ",";
			}
			prau += "\"total\":"+ paginas;
			prau += "}";
			result = "{ \"data\":" + plantas + ", \"pages\":" + prau + "}" ;
			response.status(201);
		} catch(IllegalArgumentException e) {
			response.status(400);
			result = "{\"data\":\"\",\"page\": null}";
		}
		return result;
	}
	
	public String searchResultados(Request request, Response response) {
		String resultados = request.queryParams("results");
		String[] resultadosspl = resultados.split(",");
		
		String result = "ERRO: Planta nao encontrada";
		response.status(404);
		
		for(int i = 0; i < resultadosspl.length; i++) {
			Planta index = plantadao.searchSlug(resultadosspl[i]);
			if(index!=null) {
				result = "{"
						+ "\"plantId\": \"" + index.getPlantId()   +"\","
						+ "\"slug\": \"" + index.getSlug()   +"\","
						+ "\"Cresc_forma\": \"" + index.getCresc_forma()   +"\","
						+ "\"Cresc_taxa\": \"" + index.getCresc_taxa()   +"\","
						+ "\"image_url\": \"" + index.getImagemurl()   +"\","
						+ "\"luz_solar\": " + index.getLuz_solar()   +","
						+ "\"nomecien\": \"" + index.getNomecien()   +"\","
						+ "\"nomepop\": \"" + index.getNome_pop()   +"\","
						+ "\"ph_max\": " + index.getPh_max()   +","
						+ "\"ph_min\": " + index.getPh_min()   +","
						+ "\"solo_nutrients\": " + index.getSolo_nutrientes()   +","
						+ "\"solo_salinidade\": " + index.getSolo_salinidade()   +","
						+ "\"solo_textura\": " + index.getSolo_textura()   +","
						+ "\"solo_umidade\": " + index.getSolo_umidade()   
						+ "}";
				response.status(200);
				i = resultadosspl.length + 100;
			}
		}
		
		return result;
	}
	
	
	public String searchName(Request request, Response response) {
		String result = "";
		String plantas = "";
		String nome = request.params(":name");
		String paginaStr = (request.queryParams("page"));
		int pagina = 0;
		if(paginaStr != null && !paginaStr.isEmpty()) {
			try {
				pagina = Integer.parseInt(paginaStr);
			} catch (NumberFormatException e) {
				pagina = 0;
			}
		}
		try {
			ArrayList<Planta> lista = plantadao.SearchByName(nome);
			if(lista == null) {
				throw new Exception("Pesquisa sem resultados");
			}
			int paginas = (int) Math.ceil(lista.size()/20);
			
			
			if(paginas < pagina) {
				throw new IllegalArgumentException("Erro: Pagina nao existe");
			}
			plantas += "[";
			for(int i = pagina*20; i < 20*(pagina+1) && i < lista.size(); i++) {
				Planta index = lista.get(i);
				plantas += "{"
						+ "\"plantId\": \"" + index.getPlantId()   +"\","
						+ "\"slug\": \"" + index.getSlug()   +"\","
						+ "\"Cresc_forma\": \"" + index.getCresc_forma()   +"\","
						+ "\"Cresc_taxa\": \"" + index.getCresc_taxa()   +"\","
						+ "\"image_url\": \"" + index.getImagemurl()   +"\","
						+ "\"luz_solar\": " + index.getLuz_solar()   +","
						+ "\"nomecien\": \"" + index.getNomecien()   +"\","
						+ "\"nomepop\": \"" + index.getNome_pop()   +"\","
						+ "\"ph_max\": " + index.getPh_max()   +","
						+ "\"ph_min\": " + index.getPh_min()   +","
						+ "\"solo_nutrients\": " + index.getSolo_nutrientes()   +","
						+ "\"solo_salinidade\": " + index.getSolo_salinidade()   +","
						+ "\"solo_textura\": " + index.getSolo_textura()   +","
						+ "\"solo_umidade\": " + index.getSolo_umidade()   
						+ "}";
				if(i + 1 < 20*(pagina+1) && i + 1 < lista.size() ) {
					plantas +=",";
				}
				
			}
			plantas+="]";
			String prau = "{";
			if(pagina > 0) {
				prau += "\"last\":" + (pagina-1) + ","; 
			}
			prau += "\"current\":" + (pagina) + ",";
			if(pagina < paginas ) {
				prau += "\"next\": " + (pagina +1) + ",";
			}
			prau += "\"total\":"+ paginas;
			prau += "}";
			result = "{ \"data\":" + plantas + ", \"pages\":" + prau + "}" ;
			response.status(201);
		} catch(IllegalArgumentException e) {
			response.status(400);
			result = "{\"data\": \"\",\"page\": null}";
		} catch(Exception e) {
			response.status(404);
			result = "{\"data\": null}";
		}
		return result;
	}
	
}
