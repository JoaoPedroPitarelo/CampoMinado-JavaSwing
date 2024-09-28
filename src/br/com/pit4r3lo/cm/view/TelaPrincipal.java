package br.com.pit4r3lo.cm.view;

import javax.swing.JFrame;
import javax.swing.UIManager;

import br.com.pit4r3lo.cm.model.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame { 

	public TelaPrincipal() {
		Tabuleiro tabuleiro = new Tabuleiro(18, 30, 40); // seta o tamanho e a dificuldade
		PainelTabuleiro painelTabuleiro =  new PainelTabuleiro(tabuleiro);
		
		add(painelTabuleiro); 
		
		setTitle("Campo Minado");
		setSize(783, 507);
		setLocationRelativeTo(null); 
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
		setResizable(false);
		setVisible(true); 
		
		try {
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TelaPrincipal();
	}
}
