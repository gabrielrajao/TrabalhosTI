package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

import model.Agenda;


public class AgendaDAO extends DAO{

	public AgendaDAO() {
		super();
		conectar();
		
		
	}
	
	public void finalize() {
		close();
	}
	
	public ArrayList<Agenda> getCalendario(int usuario) throws SQLException{
		ArrayList<Agenda> result = null;
		String sql = "SELECT * FROM agenda WHERE usuario = ?";
		PreparedStatement st = conexao.prepareStatement(sql);
		st.setInt(1, usuario);
		ResultSet rs = st.executeQuery();
		if(rs.next()) {
			result = new ArrayList<Agenda>();
			Agenda p = new Agenda(rs.getInt("usuario"),rs.getInt("planta"), rs.getDate("datainicio").toLocalDate() ,rs.getString("nome"), rs.getString("rega")
					,rs.getString("poda"), rs.getString("exposicao"), rs.getString("descricao"));
			result.add(p);
			while(rs.next()) {
				p = new Agenda(rs.getInt("usuario"),rs.getInt("planta"), rs.getDate("datainicio").toLocalDate() ,rs.getString("nome"), rs.getString("rega")
						,rs.getString("poda"), rs.getString("exposicao"), rs.getString("descricao"));
				result.add(p);
			}
		}
		
		return result;
	}
	
	public boolean fitJardim(int userId, int planta) {
		boolean result = true;
		try {
			
			String sql = "SELECT * FROM agenda WHERE planta = ? and usuario = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, planta);
			st.setInt(2, userId);
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				result = false;
			}
			st.close();
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return result;	
	}
	
	
	
	public boolean nomeAvaliable(String nome) {
		boolean result = true;
		try {
			
			String sql = "SELECT * FROM agenda WHERE nome = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, nome);
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				result = false;
			}
			st.close();
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return result;	
	}
	
	public boolean Inserir(Agenda x) throws SQLException {
		boolean status = false;
			String sql = "INSERT INTO agenda (planta,usuario,nome, descricao, exposicao, poda, rega,datainicio )"
					+ "VALUES (?, ? , ? ,?, ? , ? , ?, ?)";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, x.getPlanta());
			st.setInt(2, x.getUsuario());
			st.setString(3, x.getNome());
			st.setString(4, x.getDescricao());
			st.setString(5, x.getExposicao());
			st.setString(6, x.getPoda());
			st.setString(7, x.getRega());
			st.setDate(8, Date.valueOf( x.getDataInicio()));
			st.executeUpdate();
			st.close();
			status = true;
			
		return status;
	}
	
	public boolean update(Agenda x) throws SQLException {
		boolean status = false;
			String sql = "UPDATE agenda SET nome = ? , descricao = ?, exposicao = ?, poda = ? , rega = ? "
					+ "WHERE planta = ? AND usuario = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, x.getNome());
			st.setString(2, x.getDescricao());
			st.setString(3, x.getExposicao());
			st.setString(4, x.getPoda());
			st.setString(5, x.getRega());
			st.setInt(6, x.getPlanta());
			st.setInt(7, x.getUsuario());
			st.executeUpdate();
			st.close();
			status = true;
		return status;
	}
	
	public boolean delete(int planta, int user) throws SQLException {
		boolean status = false;

			String sql = "DELETE FROM agenda "
					+ "WHERE planta = ? AND usuario = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1,planta);
			st.setInt(2, user);
			st.executeUpdate();
			st.close();
			status = true;	
		return status;
	}
}
