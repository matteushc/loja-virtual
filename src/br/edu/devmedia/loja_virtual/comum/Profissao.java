package br.edu.devmedia.loja_virtual.comum;

public enum Profissao {

	
	
	PROFESSOR(1, "Professor"), 
	PADEIRO(2, "Padeiro"),
	JORNALISTA(3, "Jornalista"),
	MOTORISTA(4, "Motorista");
	
	private Integer codigo;
	private String descricao;
	
	
	private Profissao(Integer codigo, String descricao){
		this.codigo = codigo;
		this.descricao = descricao;
	}


	public Integer getCodigo() {
		return codigo;
	}


	public String getDescricao() {
		return descricao;
	}
	
	
	
}
