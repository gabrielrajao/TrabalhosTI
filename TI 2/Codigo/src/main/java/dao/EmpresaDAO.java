package dao;

import model.Empresa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class EmpresaDAO extends DAO {
	//conecta pelo DAO
	public EmpresaDAO() {
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
                "COPY empresa FROM STDIN (FORMAT csv, HEADER)", 
                new BufferedReader(new FileReader("./Empresas.csv"))
                );
        }catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    
	}
	public boolean isEmpty() {
		boolean result = true;
		String sql = "SELECT * FROM empresa";
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
	
	//fecha a conexao
	public void finalize() {
		close();
	}
	
	//checa se login esta disponivel
	public boolean loginAvaliable(String login) {
		//valor de retorno (resultado da funcao)
		boolean result = true;
		try {
			//String sql para preparo
			//IMPORTANTE: o ? e essencial contra injecoes sql
			String sql = "SELECT * FROM empresa WHERE login= ?";
			//Prepara a chamada sql
			PreparedStatement st = conexao.prepareStatement(sql);
			//"Substitui" o ? pelo login fornecido
			st.setString(1, login);
			//executa a chamada
			ResultSet rs = st.executeQuery();
			
			//se tiver next, ja possui alguem com esse nome
			if(rs.next()) {
				result = false;
			}
			//fecha a conexao, bom para seguranca
			st.close();
		}catch (Exception e) {
			//printa no console qualquer excecao
			System.err.println(e.getMessage());
		}
		return result;	
	}
	
	//mesma coisa que o loginAvaliable
	public boolean emailAvaliable(String email) {
		boolean result = true;
		try {
			
			String sql = "SELECT * FROM empresa WHERE email = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, email);
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

    
    
	//atualiza informacoes da empresa
    public boolean update(Empresa empresa) {
		boolean status = false;
		try {  
			//preparo contra injecao sql
			String sql = "UPDATE empresa SET email = ? , login = ? , senha = ?, salt = ?  WHERE compayId = ? ";
			PreparedStatement st = conexao.prepareStatement(sql);
			
			//criptografia (gera o salt aleatorio para codificacao hashpw)
            String salt = BCrypt.gensalt();
            //adiciona o salt a senha (metodo comum)
		    String senhaWSalt = salt + empresa.getSenha();
		    
		    //codifica a senha com o salt, usando salt (segundo parametro)
		    String senha = BCrypt.hashpw(senhaWSalt, salt);
		    
		    //substitui a primeira interrogacao pelo email
            st.setString(1, empresa.getEmail());
            //substitui segunda interrogacao pelo login
            st.setString(2, empresa.getLogin());
            //substitui terceira interrogacao pela senha
            st.setString(3, senha);
            //substitui quarta interrogacao pelo salt (importante armazenar visto que juntamos a senha)
            st.setString(4, salt);
            //substitui a quinta interrogacao por uma INT(id da empresa)
            st.setInt(5, empresa.getCompanyId());
            //executa pedido
			st.executeUpdate();
			//fecha conexao
			st.close();
			//caso nao ocorram excecoes define o status como true
			status = true;
		
		}
		//lida com erros no sql
		catch (SQLException u) {
			//se ocorrer erro, status = false
			status = false;
		}
		return status;
	}
    
    //funcao deleta empresa
    public boolean delete(int id) {
    	
		boolean status = false;
		try {
			//string sql contra injecao sql
            String sql = "DELETE FROM empresa WHERE companyId = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			
            st.setInt(1, id);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
    
    
	public boolean insert(Empresa empresa) {
		boolean status = false;
		try {
			String sql = "INSERT INTO empresa (email,login,senha, salt) "
		               + "VALUES (?,?,?,?)";
			PreparedStatement st = conexao.prepareStatement(sql);
			//metodo de criptografia igual ao da funcao update
		    String salt = BCrypt.gensalt();
		    String senhaWSalt = salt + empresa.getSenha();
		    String senha = BCrypt.hashpw(senhaWSalt, salt);
			st.setString(1, empresa.getEmail() );
			st.setString(2, empresa.getLogin() );
			st.setString(3, senha );
			st.setString(4, salt);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		} 
		
		return status;
	}
    
   //pega empresa pelo id
    public Empresa get(int id) {
		Empresa empresa = null;
		
		try {
			String sql = "SELECT * FROM empresa WHERE companyId= ? ";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, id);
			ResultSet rs = st.executeQuery();	
	        if(rs.next()){            
	        	 empresa = new Empresa(rs.getInt("companyId"), rs.getString("email"), rs.getString("login"), rs.getString("senha"), rs.getString("salt"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			empresa = null;
		}
		return empresa;
	}

    //pega empresa pelo login
    public Empresa getByLogin(String login) {
		Empresa empresa = null;
		
		try {
			String sql = "SELECT * FROM empresa WHERE login = ? ";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, login);
			ResultSet rs = st.executeQuery();	
	        if(rs.next()){            
	        	 empresa = new Empresa(rs.getInt("companyId"), rs.getString("email"), rs.getString("login"), rs.getString("senha"),rs.getString("salt"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			empresa = null;
		}
		return empresa;
	} 
    
    //pega empresa pelo email
   public Empresa getByEmail(String email) {
		Empresa empresa = null;
		
		try {
			String sql = "SELECT * FROM empresa WHERE email = ? ";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, email);
			ResultSet rs = st.executeQuery();	
	        if(rs.next()){            
	        	 empresa = new Empresa(rs.getInt("companyId"), rs.getString("email"), rs.getString("login"), rs.getString("senha"),rs.getString("salt"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			empresa = null;
		}
		return empresa;
	} 
    
   //autorizacao da empresa
    public Empresa Auth(Empresa empresa){
    	Empresa result = null;
    	//tenta encontrar uma empresa considerando que a string salva no atributo login do objeto recebido 
    	//por parametro realmente e um login
        Empresa found = getByLogin(empresa.getLogin());
        //caso found seja diferente de null, era login
        if(found != null){
        	
        	//senha salva e criptografada no banco de dados
            String pw = found.getSenha();
            //somando o salt a senha digitada pela empresa
            String pwcheck = found.getSalt() + empresa.getSenha();
            
            //checa se a string pwcheck e igual a string criptografada pw
            if(BCrypt.checkpw(pwcheck,pw)){
            	//caso seja, result a encontrada
                result = found;
            }
        } else {
        	//tenta encontrar uma empresa considerando que a string salva no atributo login do objeto recebido 
        	//por parametro e, na verdade, um email
        	found = getByEmail(empresa.getLogin());
        	if(found != null) {
        		//mesma coisa que a do login
                String pw = found.getSenha();
                String pwcheck = found.getSalt()+ empresa.getSenha();
                if(BCrypt.checkpw(pwcheck,pw)){
                    result = found;
                }
        	}
        }
        
        //se a autenticacao falhar, vai retornar null
        //se a autenticacao der certo, vai retornar um objeto da classe Empresa com as informacoes da empresa
        return result;
    }
	
}
