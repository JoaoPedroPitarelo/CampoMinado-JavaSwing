package br.com.pit4r3lo.cm.model;

public class ResultadoEvento {

	private final boolean ganhou;
	
	public ResultadoEvento(boolean resultado) {
		this.ganhou = resultado; 
	}
	
	public boolean isGanhou() {
		return ganhou;
	}
}
