package br.com.pit4r3lo.cm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador { 
	
	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

	public int getQuantidadeLinhas() { return linhas; }

	public int getQuantidadeColunas() { return colunas; }

	public int getQuantidadeMinas() { return minas; }

	public List<Campo> getCampos() { return campos; }

	public Tabuleiro(int quantidadeLinhas, int quantidadeColunas, int quantidadeMinas) {
		this.linhas = quantidadeLinhas;
		this.colunas = quantidadeColunas;
		this.minas = quantidadeMinas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public int getMinas() {
		return minas;
	}
	
	public void paraCadaCampo(Consumer<Campo> funcao) {
		campos.stream()
			.forEach(funcao);
	}

	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(boolean resultado) {
		observadores.stream()
			.forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}

	public void abrir(int linha, int coluna) {
		campos.parallelStream()
				.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
				.findFirst() 
				.ifPresent(c -> c.abrir());
	}
		
	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst() 
			.ifPresent(c -> c.alternarMarcacao());
	}
	
	private void gerarCampos() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				Campo campo = new Campo(i, j);
				campo.registrarObservador(this);
				campos.add(campo);
			}
		}
	}

	private void associarVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void sortearMinas() {
		int minasArmadas = 0;
		Predicate<Campo> minado = campo -> campo.isMinado();
		
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = (int) campos.stream().filter(minado).count();
		} while(minasArmadas < minas);
		
	}
	
	public boolean objetivoAlcancado() { 
		return campos.stream().allMatch(c -> c.objetivoAlcancadO());
	}

	public void reiniciarJogo() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
	@Override
	public void eventoOcorreu(Campo c, CampoEvento e) {
		if(e == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if(objetivoAlcancado()) {
			notificarObservadores(true);
		}
	}
	
	private void mostrarMinas() {		
		campos.stream()
			.filter(c -> c.isMinado())
			.filter(c -> !c.isMarcado())
			.forEach(c -> c.setAberto(true));
	}
}
