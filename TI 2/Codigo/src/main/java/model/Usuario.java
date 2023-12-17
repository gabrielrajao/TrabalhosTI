package model;

public class Usuario {
	private int userId;
	private String email;
	private String login;
	private String senha;
	private String salt;
	
	public Usuario() {
		userId = -1;
		email = "";
		login = "";
		senha = "";
		salt = "";
	}
	
	public Usuario(int id, String email, String login, String senha, String salt) {
		setUserId(id);
		setEmail(email);
		setLogin(login);
		setSenha(senha);
		setSalt (salt);
	}
	
	public void setUserId(int id) {
		this.userId = id;
	}
	public int getUserId() {
		return this.userId;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	public String getLogin() {
		return this.login;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return this.email;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getSenha() {
		return this.senha;
	}
	
	public void setSalt (String salt) {
		this.salt = salt;
	}
	
	public String getSalt() {
		return this.salt;
	}
}
