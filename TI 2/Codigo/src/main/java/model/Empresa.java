package model;

public class Empresa {
	private int companyId;
	private String email;
	private String login;
	private String senha;
	private String salt;
	
	public Empresa() {
		companyId = -1;
		email = "";
		login = "";
		senha = "";
		salt = "";
	}
	
	public Empresa(int companyId, String email, String login, String senha, String salt) {
		setCompanyId(companyId);
		setEmail(email);
		setLogin(login);
		setSenha(senha);
		setSalt (salt);
	}
	
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int getCompanyId() {
		return this.companyId;
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
