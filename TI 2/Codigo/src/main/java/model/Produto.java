package model;


public class Produto {
	private int prodid;
	private String funcao;
	private float preco;
	private String tipo;
	private String nome;	
	private String produrl;
	private int codEmpresa;
	
	public Produto() {
		prodid = -1;
		funcao = "";
		preco = 0.00F;
		tipo = "";
		nome = "";
		produrl = "";
		codEmpresa = 0;
	}

	public Produto(int prodid, String funcao, float preco, String tipo, String nome, String produrl, int codEmpresa) {
		setPRODID(prodid);
		setFuncao(funcao);
		setPreco(preco);
		setTipo(tipo);
		setNome(nome);
		setProdUrl(produrl);
		setCodEmpresa(codEmpresa);
	}		
	
	public int getPRODID() {
		return prodid;
	}

	public void setPRODID(int prodid) {
		this.prodid = prodid;
	}

	
	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao= funcao;
	}

	public float getPreco() {
		return preco;
	}

	public void setPreco(float preco) {
		this.preco = preco;
	}

	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;	}

	public String getProdUrl() {
		return produrl;
	}
	
	public void setProdUrl(String produrl) {
		this.produrl = produrl;
	}

	public int getCodEmpresa() {
		return codEmpresa;
	}
	
	public void setCodEmpresa(int codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Produto: " + funcao + "   Preço: R$" + preco + "   Tipo.: " + tipo+ "   Nome: "
				+ nome  + "   Url Produto: " + produrl;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getPRODID() == ((Produto) obj).getPRODID());
	}	
}
