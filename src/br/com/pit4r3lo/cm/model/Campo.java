package br.com.pit4r3lo.cm.model;

import java.util.ArrayList;
import java.util.List;

public class Campo {

	private final int coluna;
	private final int linha;
	
	private boolean aberto  = false;
	private boolean minado  = false;
	private boolean marcado = false;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<CampoObservador>();

	Campo(int linha, int coluna) { 
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public boolean isMarcado() { return marcado; }
	
	public boolean isAberto() { return aberto; }
	
	public void setAberto(boolean aberto) { 
		this.aberto = aberto;
		
		if(aberto) {
			notificarObservadores(CampoEvento.ABRIR);			
		}
	}
		
	public boolean isFechado() { return !isAberto(); }
	
	public boolean isMinado() { return minado; }

	public void setMinado(boolean minado) { this.minado = minado; }
	
	public List<Campo> getVizinhos() { return vizinhos; }

	public void setVizinhos(List<Campo> vizinhos) { this.vizinhos = vizinhos; }

	public int getColuna() { return coluna; }

	public int getLinha() { return linha; }
	
	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(CampoEvento evento) { // qual evento aconteceu
		observadores.stream().forEach(obs -> obs.eventoOcorreu(this, evento)); // this, o próprio Campo
	}
	
	boolean objetivoAlcancadO() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		
		return desvendado || protegido;
	}
	
	public int minasNaVizinhanca() {
		return (int) vizinhos.stream()
						.filter(v -> v.isMinado())
						.count();
	}
	
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}
	
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;
		
		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		}
		return false;
	}
	
	public void alternarMarcacao() {
		if (!aberto) {
			marcado = !marcado; 
								
			if(marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		} 						
	}
	
	public boolean abrir() {
		if (!aberto && !marcado) { 
			
			if (minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}

			setAberto(true);
			
			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir()); 
			}
			
			return true;
		} else {
			return false;			
		}
	}
	
	void minar() {
		minado = true;
	}
	
	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
}
