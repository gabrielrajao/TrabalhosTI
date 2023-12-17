package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import dao.AgendaDAO;
import model.Agenda;
import model.Planta;
import model.Usuario;
import spark.Request;
import spark.Response;

public class AgendaService {
	
	private AgendaDAO agendadao = new AgendaDAO();
	
	
	
	public String getCalendario(Request request, Response response, UsuarioService UServ, PlantaService pserv) {
		String result = "";
		String token = request.headers("Authorization");
		Usuario user = UServ.UserToken(token);
		
		try{
			if(user == null) {
				
				throw new SQLException("Token nao validado/expirado");
			}
			ArrayList<Agenda> lista = agendadao.getCalendario(user.getUserId());

			if(lista == null) {
			
				throw new Exception("ERRO: Nenhuma agenda encontrada");
			}
			result += "[";
			for(int i = 0; i < lista.size(); i++) {
				if(i > 0) {
					result += ",";
				}
				Agenda index = lista.get(i);
				Planta indexp = pserv.getPlanta(index.getPlanta());
			    LocalDate r = LocalDate.now();
			    long dias = index.getDataInicio().datesUntil(r).count();
				result += "{"
						+ "\"planta\": \"" + index.getPlanta()   +"\","
						+ "\"usuario\":"+index.getUsuario()+","
						+ "\"datainicio\":"+dias+","
						+ "\"expo\": \""+index.getExposicao()+"\","
						+ "\"poda\": \""+index.getPoda()+"\","
						+ "\"rega\": \""+index.getRega()+"\","
						+ "\"nomeplanta\": \""+indexp.getNome_pop()+"\","
						+ "\"imagemurl\": \""+indexp.getImagemurl()+"\""
								+ "}";
						
				
			}
			result += "]";
		}
		catch (SecurityException e) {
			response.status(401);
			result = e.getMessage();
		}
		catch (SQLException e) {
			response.status(505);
			result = "Erro interno desconhecido";
		}
		catch (Exception e) {
			response.status(404);
			result = e.getMessage();
		}
		
		return result;
	}
	
	public String Inserir(Request request, Response response, UsuarioService UServ) {
		String result = "";

		try {
			String token = request.headers("Authorization");
			Usuario user = UServ.UserToken(token);
			int planta = Integer.parseInt(request.params(":planta"));
			String nome = request.queryParams("nome");
			String descricao = request.queryParams("descricao");
			/*
			System.out.println(request.queryParams("lsi"));
			System.out.println(request.queryParams("lsfim"));
			System.out.println(request.queryParams("rh"));
			System.out.println(request.queryParams("rfrq"));
			System.out.println(request.queryParams("ph"));
			System.out.println(request.queryParams("pfrq"));
			*/
			int luzsol_i = Integer.parseInt(request.queryParams("lsi"));
			int luzsol_fim = Integer.parseInt(request.queryParams("lsfim"));
			int rega_h = Integer.parseInt(request.queryParams("rh"));
			int rega_frq = Integer.parseInt(request.queryParams("rfrq"));
			int poda_h = Integer.parseInt(request.queryParams("ph"));
			int poda_frq = Integer.parseInt(request.queryParams("pfrq"));
			
			if(user == null) {
				throw new SecurityException("Token inválido/expirado!");
			}
			if(nome.length() <= 6) {
				throw new IllegalArgumentException("Nome: invalido");
			}
			if(descricao.length() <= 30) {
				throw new IllegalArgumentException("Descricao: invalida");
			}
			

			
			if(poda_h < 1 || poda_h > 24) {
				throw new IllegalArgumentException("Horario de poda: invalido");
			}
			
			
			if(poda_frq != 0 && poda_frq != 7 && poda_frq != 14 && poda_frq != 28 && poda_frq != 31 ) {
				throw new IllegalArgumentException("Frequencia de poda: invalida");
			}
			if(poda_frq == 0) {
				poda_h = 0;
			}
			if(luzsol_i < 6 || luzsol_i > 17 || luzsol_fim < 6 || luzsol_fim > 17) {
				throw new IllegalArgumentException("Horario de luz: invalido");
			}
			if(rega_h < 1 || rega_h > 24) {
				throw new IllegalArgumentException("Horario de rega: invalido");
			}
			if(rega_frq < 1 || rega_frq > 7) {
				throw new IllegalArgumentException("Frequencia de rega: invalida");
			}
			if(agendadao.nomeAvaliable(nome) == false) {
				throw new IllegalArgumentException("Nome: indisponivel");
			}
			if(luzsol_i > luzsol_fim) {
				throw new IllegalArgumentException("Horario de luz: invalido");
			}
			if(agendadao.fitJardim(user.getUserId(), planta) == false) {
				throw new IllegalArgumentException("Jardim: Planta já inserida no jardim");
			}
			String poda = "0,0";
			if(poda_h != 0) {
				poda = poda_h + "," + poda_frq;
			}
			LocalDate datain = LocalDate.now().minusDays(20);
			String rega = rega_h + "," + rega_frq;
			String luz = luzsol_i + " - " + luzsol_fim;
			Agenda x = new Agenda(user.getUserId(),planta, datain ,nome, rega,  poda, luz, descricao);
			if(agendadao.Inserir(x) == true) {
				response.status(201);
				result = "Agenda adicionada com sucesso!";
			} else {
				response.status(505);
				result = "Erro interno desconhecido";
			}
			
			
			
			
		}
		catch (SecurityException e) {
			response.status(401);
			result = e.getMessage();
		}
		catch (NumberFormatException e) {
			response.status(412);
			result = e.getMessage();
		}
		catch (IllegalArgumentException e) {
			response.status(400);
			result = e.getMessage();
		}
		catch (SQLException e) {
			response.status(505);
			result = "Erro interno desconhecido";
		}

		
		return result;
	}
	
	public String delete(Request request, Response response, UsuarioService UServ) {
        int planta = Integer.parseInt( request.params(":planta") );
        //requisição do token e verificacao de usuário
        String token = request.headers("Authorization");
		Usuario user = UServ.UserToken(token);
		String resp = "";
        
        try {
        	if(user == null) {
    			throw new SecurityException("Token inválido/expirado!");
    		}
            int userid = user.getUserId();
            
            
            if (planta !=  -1) {
            	try {
    				agendadao.delete(planta, userid );
    			} catch (SQLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                response.status(200); // success
                resp = "Planta (" + userid  + ") excluído!";
            } else {
                response.status(404); // 404 Not found
                resp = "Produto (" + userid  + ") não encontrado!";
            }
            
        } catch( SecurityException e) {
        	response.status(401);
        	resp = e.getMessage();
        }
        return resp;
	}
}
