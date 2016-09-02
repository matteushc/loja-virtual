package br.edu.devmedia.loja_virtual.dominio;

import br.edu.devmedia.loja_virtual.R;

public enum DashBoardListItem {

	LISTAR("Listar", R.drawable.info), 
	PESQUISAR("Pesquisar", R.drawable.search);
	
	private String titulo;
	
	private Integer idImg;

	private DashBoardListItem(String titulo, Integer idImg) {
		this.titulo = titulo;
		this.idImg = idImg;
	}
	
	
	public String getTitulo() {
		return titulo;
	}

	public Integer getIdImg() {
		return idImg;
	}
	
	
}
