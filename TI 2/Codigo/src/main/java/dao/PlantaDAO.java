package dao;

import model.Planta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import arq.Arq;


public class PlantaDAO extends DAO {
	//conecta pelo DAO
	public PlantaDAO() {
		super();
		
		conectar();
		if(getAll() == null) {
			sendCsv();
		}
		
		
	}
	
	//fecha a conexao
	public void finalize() {
		close();
	}
	

	public ArrayList<Planta> getAll(){
		String sql = "SELECT * FROM planta";
		ArrayList<Planta> lista = new ArrayList<Planta>();
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()) {
				Planta p = new Planta(rs.getInt("plantId"),rs.getString("slug"), rs.getString("nome_pop"), rs.getString("nomecien"),
						rs.getString("imagemurl"), rs.getString("cresc_forma"), rs.getString("cresc_taxa"),rs.getFloat("ph_max"),
						rs.getFloat("ph_min"), rs.getInt("luz_solar"), rs.getInt("solo_nutrientes"), rs.getInt("solo_salinidade"),
						rs.getInt("solo_textura"), rs.getInt("solo_umidade"));
				lista.add(p);
				while(rs.next()) {
							p = new Planta(rs.getInt("plantId"),rs.getString("slug"), rs.getString("nome_pop"), rs.getString("nomecien"),
							rs.getString("imagemurl"), rs.getString("cresc_forma"), rs.getString("cresc_taxa"),rs.getFloat("ph_max"),
							rs.getFloat("ph_min"), rs.getInt("luz_solar"), rs.getInt("solo_nutrientes"), rs.getInt("solo_salinidade"),
							rs.getInt("solo_textura"), rs.getInt("solo_umidade"));
							lista.add(p);
				}
			} else {
				lista = null;
			}
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return lista;
		
		
	}
	
	public void sendCsv() {
		
        try {
        	BaseConnection a = (BaseConnection) conexao;
        	new CopyManager(a)
            .copyIn(
                "COPY planta FROM STDIN (FORMAT csv, HEADER)", 
                new BufferedReader(new FileReader("./plantasFIM.csv"))
                );
        }catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    
	}
	
	public Planta getPlanta(int id) {
		Planta result = null;
		try {
			String sql = "SELECT * FROM planta WHERE plantId=?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				result= new Planta(rs.getInt("plantId"),rs.getString("slug"), rs.getString("nome_pop"), rs.getString("nomecien"),
						rs.getString("imagemurl"), rs.getString("cresc_forma"), rs.getString("cresc_taxa"),rs.getFloat("ph_max"),
						rs.getFloat("ph_min"), rs.getInt("luz_solar"), rs.getInt("solo_nutrientes"), rs.getInt("solo_salinidade"),
						rs.getInt("solo_textura"), rs.getInt("solo_umidade"));


			} 
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	public Planta searchSlug(String slug) {
		Planta result = null;
		try {
			String sql = "SELECT * FROM planta WHERE slug=?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, slug);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				result= new Planta(rs.getInt("plantId"),rs.getString("slug"), rs.getString("nome_pop"), rs.getString("nomecien"),
						rs.getString("imagemurl"), rs.getString("cresc_forma"), rs.getString("cresc_taxa"),rs.getFloat("ph_max"),
						rs.getFloat("ph_min"), rs.getInt("luz_solar"), rs.getInt("solo_nutrientes"), rs.getInt("solo_salinidade"),
						rs.getInt("solo_textura"), rs.getInt("solo_umidade"));


			} 
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	public ArrayList<Planta> SearchByName(String name){
		String sql = "SELECT * FROM planta WHERE nome_pop ILIKE ?";
		ArrayList<Planta> lista = new ArrayList<Planta>();
		try {
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, "%"+name + "%");
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				Planta a = new Planta(rs.getInt("plantId"),rs.getString("slug"), rs.getString("nome_pop"), rs.getString("nomecien"),
						rs.getString("imagemurl"), rs.getString("cresc_forma"), rs.getString("cresc_taxa"),rs.getFloat("ph_max"),
						rs.getFloat("ph_min"), rs.getInt("luz_solar"), rs.getInt("solo_nutrientes"), rs.getInt("solo_salinidade"),
						rs.getInt("solo_textura"), rs.getInt("solo_umidade"));
				lista.add(a);
				while(rs.next()) {
					Planta p = new Planta(rs.getInt("plantId"),rs.getString("slug"), rs.getString("nome_pop"), rs.getString("nomecien"),
							rs.getString("imagemurl"), rs.getString("cresc_forma"), rs.getString("cresc_taxa"),rs.getFloat("ph_max"),
							rs.getFloat("ph_min"), rs.getInt("luz_solar"), rs.getInt("solo_nutrientes"), rs.getInt("solo_salinidade"),
							rs.getInt("solo_textura"), rs.getInt("solo_umidade"));
					lista.add(p);
				}
			} else {
				lista = null;
			}
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return lista;
		
		
	}

	/*
	public static void main (String[] args) {
		Arq.openRead("PlantasPop.txt", "UTF-8");
		ArrayList<String> lista = new ArrayList<String>();
		while(Arq.hasNext()) {
		    String line = Arq.readLine();
		    lista.add(line.split(" - ")[1]);
		    
		}
		Arq.close();
		Arq.openRead("Dados_Plantas_BACKUP.csv");
		ArrayList<String> lista2 = new ArrayList<String>();
		while(Arq.hasNext()) {
		    String line = Arq.readLine();
		    lista2.add(line);
		    
		}
		Arq.close();
		Arq.openWrite("PlantasFinal.csv", "UTF-8");
		Arq.println(lista2.get(0) + ",nome_pop");
		int r = 0;
		for(int i = 1; i < lista2.size(); i++) {
			Arq.println(lista2.get(i) + "," + lista.get(r));
			r++;
		}
		Arq.close();
	} */
	
}
