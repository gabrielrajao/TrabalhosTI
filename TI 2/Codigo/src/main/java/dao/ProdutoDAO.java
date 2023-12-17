
//ProdutoDAO


package dao;

import model.Produto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;


public class ProdutoDAO extends DAO {	
	public ProdutoDAO() {
		super();
		conectar();
		if(isEmpty() == true) {
			sendCsv();
		}
	}
	public void sendCsv() {
		
        try {
        	BaseConnection a = (BaseConnection) conexao;
        	new CopyManager(a)
            .copyIn(
                "COPY produto FROM STDIN (FORMAT csv, HEADER)", 
                new BufferedReader(new FileReader("./Produtos.csv"))
                );
        }catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    
	}
	
	
	public void finalize() {
		close();
	}
	public boolean isEmpty() {
		boolean result = true;
		String sql = "SELECT * FROM produto";
		try {
			PreparedStatement st = conexao.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				result = false;
			}
		} catch (Exception e) {
			
		}
		return result;
	}
	
	public boolean insert(Produto produto) {
		boolean status = false;
		try {
			String sql = "INSERT INTO produto (funcao, preco, tipo, nome, produrl, empresa) "
		               + "VALUES ('" + produto.getFuncao() + "', "
		               + produto.getPreco() + ",' " + produto.getTipo() + "', '"
		               + produto.getNome() + "', '" +produto.getProdUrl()  + "'," + 
		               produto.getCodEmpresa()+ ")";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
			
		}
		return status;
	}

	
	public ArrayList<Produto> getEmpresa(int codEmpresa) {
		ArrayList<Produto> produto = new ArrayList<Produto>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM produto WHERE empresa="+codEmpresa;
			ResultSet rs = st.executeQuery(sql);	
	        while(rs.next()){
	        	 Produto product = new Produto(rs.getInt("prodid"), rs.getString("funcao"), (float)rs.getDouble("preco"), 
	                				   rs.getString("tipo"), 
	        			               rs.getString("nome"),
	        			               rs.getString("produrl"),
	        			               rs.getInt("empresa"));
	        	 produto.add(product);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produto;
	}
	
	
	public List<Produto> get() {
		return get("");
	}

	
	public List<Produto> getOrderByProdId() {
		return get("prodid");		
	}
	
	
	public List<Produto> getOrderByFuncao() {
		return get("funcao");		
	}
	
	
	public List<Produto> getOrderByPreco() {
		return get("preco");		
	}
	
	
	private List<Produto> get(String orderBy) {
		List<Produto> produtos = new ArrayList<Produto>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM produto" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Produto p = new Produto(rs.getInt("prodid"), rs.getString("funcao"), (float)rs.getDouble("preco"), 
	        			                rs.getString("tipo"),
	        			                rs.getString("nome"),
	        			                rs.getString("produrl"),
	        			                rs.getInt("empresa"));
	            produtos.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produtos;
	}
	
	
	public boolean update(Produto produto) {
		boolean status = false;
		try {  
			String sql = "UPDATE produto SET funcao = '" + produto.getFuncao() + ", "
					   + "preco = " + produto.getPreco() + ", " 
					   + "tipo= " + produto.getTipo() + ","
					   + "nome = " + produto.getNome() + "," 
					   + "produrl = " + produto.getProdUrl() + "codEmpresa = "
					   + produto.getCodEmpresa() + " WHERE prodid = " + produto.getPRODID();
			PreparedStatement st = conexao.prepareStatement(sql);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public boolean delete(int prodid) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM produto WHERE prodid = " + prodid);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	//Método get para pegar recebendo o id e selecionado onde o prodid está
		public Produto get(int prodid) {
			Produto produto = null;
			
			try {
				String sql = "SELECT * FROM produto WHERE prodid=?";
				PreparedStatement st = conexao.prepareStatement(sql);
				st.setInt(1,prodid);
				ResultSet rs = st.executeQuery();	
		        if(rs.next()){            
		        	 produto = new Produto(rs.getInt("prodid"), rs.getString("funcao"), (float)rs.getDouble("preco"), 
		                				   rs.getString("tipo"), 
		        			               rs.getString("nome"),
		        			               rs.getString("produrl"),
		        			               rs.getInt("empresa"));
		        }
		        st.close();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			return produto;
		}
}