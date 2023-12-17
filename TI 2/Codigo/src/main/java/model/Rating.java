package model;

public class Rating {
	private int usuario;
	private int produto;
	private int rating;
	
	public Rating(){
		usuario = -1;
		produto = -1;
		rating = -1;
	}
	public Rating(int usuario, int produto, int rating){
		this.usuario = usuario;
		this.produto = produto;
		this.rating = rating;
	}
	public Rating(int usuario, int produto){
		this.usuario = usuario;
		this.produto = produto;
		this.rating = -1;
	}
	
	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}
	public void setProduto(int produto) {
		this.produto = produto;
	}
	public void setRating(int rating) {
			this.rating = rating;
	}
	
	public int getUsuario() {
		return usuario;
	}
	
	public int getProduto() {
		return produto;
	}
	
	public int getRating() {
		return rating;
	}
	
	
	
	
	
	
	
}
