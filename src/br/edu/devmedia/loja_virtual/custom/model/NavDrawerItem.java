package br.edu.devmedia.loja_virtual.custom.model;

public class NavDrawerItem {

	
	private String titulo;
	
	private int icone;
	
	private String cont = "0";
	
	private boolean isContVisivel = false;
	
	
	public NavDrawerItem() {
		// TODO Auto-generated constructor stub
	}


	public NavDrawerItem(String titulo, int icone, String cont, boolean isContVisivel) {
		this.titulo = titulo;
		this.icone = icone;
		this.cont = cont;
		this.isContVisivel = isContVisivel;
	}


	public NavDrawerItem(String titulo, int icone) {
		super();
		this.titulo = titulo;
		this.icone = icone;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public int getIcone() {
		return icone;
	}


	public void setIcone(int icone) {
		this.icone = icone;
	}


	public boolean isContVisivel() {
		return isContVisivel;
	}


	public void setContVisivel(boolean isContVisivel) {
		this.isContVisivel = isContVisivel;
	}


	public String getCont() {
		return cont;
	}


	public void setCont(String cont) {
		this.cont = cont;
	}
}
