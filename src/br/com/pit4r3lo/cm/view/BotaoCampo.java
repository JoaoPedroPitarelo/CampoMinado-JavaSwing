package br.com.pit4r3lo.cm.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import br.com.pit4r3lo.cm.model.Campo;
import br.com.pit4r3lo.cm.model.CampoEvento;
import br.com.pit4r3lo.cm.model.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

	private Campo campo;
	private final Color BG_PADRAO = new Color(184, 184, 184);
	private final Color BG_MARCAR = new Color(100, 100, 230);
	private final Color BG_EXPLODIR = new Color(189, 66, 68);
	private final Color TEXTO_VERDE = new Color(0, 100, 0);
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		setBorder(BorderFactory.createBevelBorder(0));
		setOpaque(true);
		setPreferredSize(new Dimension(50, 50));
		setBackground(BG_PADRAO);
		
		addMouseListener(this); 
		campo.registrarObservador(this); 
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch(evento) {
			case ABRIR:
				aplicarEstiloAbrir();
				break;
			case MARCAR:
				aplicarEstiloMarcar();
				break;
			case EXPLODIR:
				aplicarEstiloExplodir();
				break;
			default:
				aplicaEstiloPadrao();
		}
		
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}

	private void aplicaEstiloPadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		setIcon(null);
		setText("");
	}

	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.white);
		ImageIcon icon = new ImageIcon(getClass().getResource("/br/com/pit4r3lo/cm/view/assets/icons/bomb.png"));
		setIcon(icon);
	}

	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCAR);
		setForeground(Color.WHITE);
		ImageIcon icon = new ImageIcon(getClass().getResource("/br/com/pit4r3lo/cm/view/assets/icons/flag.png"));
		setIcon(icon);
	}

	private void aplicarEstiloAbrir() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		if(campo.isMinado()) {
			setBackground(BG_EXPLODIR);
			return;
		}
		
		setBackground(BG_PADRAO);
		
		switch (campo.minasNaVizinhanca()) {
			case 1: {
				setForeground(TEXTO_VERDE);
				break;
			}
			case 2: {
				setForeground(Color.BLUE);
				break;
			}
			case 3: {
				setForeground(Color.YELLOW);
				break;
			}
			case 4: case 5: case 6: {
				setForeground(Color.RED);
				break;
			}
			default:
				setForeground(Color.PINK);
		}
		
		String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";
		setText(valor);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) { // Botão esquerdo
			campo.abrir();
		} else {
			campo.alternarMarcacao(); // qualquer outro botão do mouse
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}	
}
