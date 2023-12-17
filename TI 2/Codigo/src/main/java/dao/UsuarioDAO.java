package dao;

import model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO extends DAO {
	//conecta pelo DAO
	public UsuarioDAO() {
		super();
		conectar();
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
			String sql = "SELECT * FROM usuario WHERE login= ?";
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
			
			String sql = "SELECT * FROM usuario WHERE email = ?";
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

    
    
	//atualiza informacoes do usuario
    public boolean update(Usuario usuario) {
		boolean status = false;
		try {  
			//preparo contra injecao sql
			String sql = "UPDATE usuario SET email = ? , login = ? , senha = ?, salt = ?  WHERE userId = ? ";
			PreparedStatement st = conexao.prepareStatement(sql);
			
			//criptografia (gera o salt aleatorio para codificacao hashpw)
            String salt = BCrypt.gensalt();
            //adiciona o salt a senha (metodo comum)
		    String senhaWSalt = salt + usuario.getSenha();
		    
		    //codifica a senha com o salt, usando salt (segundo parametro)
		    String senha = BCrypt.hashpw(senhaWSalt, salt);
		    
		    //substitui a primeira interrogacao pelo email
            st.setString(1, usuario.getEmail());
            //substitui segunda interrogacao pelo login
            st.setString(2, usuario.getLogin());
            //substitui terceira interrogacao pela senha
            st.setString(3, senha);
            //substitui quarta interrogacao pelo salt (importante armazenar visto que juntamos a senha)
            st.setString(4, salt);
            //substitui a quinta interrogacao por uma INT(id de usuario)
            st.setInt(5, usuario.getUserId());
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
    
    //funcao deleta usuario
    public boolean delete(int id) {
    	
		boolean status = false;
		try {
			//string sql contra injecao sql
            String sql = "DELETE FROM usuario WHERE userId = ?";
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
    
    
	public boolean insert(Usuario usuario) {
		boolean status = false;
		try {
			String sql = "INSERT INTO usuario (email,login,senha, salt) "
		               + "VALUES (?,?,?,?)";
			PreparedStatement st = conexao.prepareStatement(sql);
			//metodo de criptografia igual ao da funcao update
		    String salt = BCrypt.gensalt();
		    String senhaWSalt = salt + usuario.getSenha();
		    String senha = BCrypt.hashpw(senhaWSalt, salt);
			st.setString(1, usuario.getEmail() );
			st.setString(2, usuario.getLogin() );
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
    
   //pega usuario pelo id
    public Usuario get(int id) {
		Usuario usuario = null;
		
		try {
			String sql = "SELECT * FROM usuario WHERE userid= ? ";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, id);
			ResultSet rs = st.executeQuery();	
	        if(rs.next()){            
	        	 usuario = new Usuario(rs.getInt("userId"), rs.getString("email"), rs.getString("login"), rs.getString("senha"), rs.getString("salt"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			usuario = null;
		}
		return usuario;
	}

    //pega usuario pelo login
    public Usuario getByLogin(String login) {
		Usuario usuario = null;
		
		try {
			String sql = "SELECT * FROM usuario WHERE login = ? ";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, login);
			ResultSet rs = st.executeQuery();	
	        if(rs.next()){            
	        	 usuario = new Usuario(rs.getInt("userId"), rs.getString("email"), rs.getString("login"), rs.getString("senha"),rs.getString("salt"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			usuario = null;
		}
		return usuario;
	} 
    
    //pega usuario pelo email
   public Usuario getByEmail(String email) {
		Usuario usuario = null;
		
		try {
			String sql = "SELECT * FROM usuario WHERE email = ? ";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, email);
			ResultSet rs = st.executeQuery();	
	        if(rs.next()){            
	        	 usuario = new Usuario(rs.getInt("userId"), rs.getString("email"), rs.getString("login"), rs.getString("senha"),rs.getString("salt"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			usuario = null;
		}
		return usuario;
	} 
    
   //autorizacao de usuario
    public Usuario Auth(Usuario user){
    	Usuario result = null;
    	//tenta encontrar um usuario considerando que a string salva no atributo login do objeto recebido 
    	//por parametro realmente e um login
        Usuario found = getByLogin(user.getLogin());
        //caso found seja diferente de null, era login
        if(found != null){
        	
        	//senha salva e criptografada no banco de dados
            String pw = found.getSenha();
            //somando o salt a senha digitada pelo usuario
            String pwcheck = found.getSalt() + user.getSenha();
            
            //checa se a string pwcheck e igual a string criptografada pw
            if(BCrypt.checkpw(pwcheck,pw)){
            	//caso seja, result a encontrada
                result = found;
            }
        } else {
        	//tenta encontrar um usuario considerando que a string salva no atributo login do objeto recebido 
        	//por parametro e, na verdade, um email
        	found = getByEmail(user.getLogin());
        	if(found != null) {
        		//mesma coisa que a do login
                String pw = found.getSenha();
                String pwcheck = found.getSalt()+ user.getSenha();
                if(BCrypt.checkpw(pwcheck,pw)){
                    result = found;
                }
        	}
        }
        
        //se a autenticacao falhar, vai retornar null
        //se a autenticacao der certo, vai retornar um objeto da classe Usuario com as informacoes do usuario
        return result;
    }
	
}
