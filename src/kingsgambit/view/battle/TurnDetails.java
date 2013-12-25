package kingsgambit.view.battle;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import kingsgambit.model.Faction;

public class TurnDetails extends JPanel {

	private static final long serialVersionUID = 1L;

	public void setTurn(Faction f) {
		phase.setText(f + "'s Turn");
	}
	
	public TurnDetails() {
		phase = new JLabel();
		movesLeft = new JLabel("2 Moves Left");
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(600, 50));
		
		add(phase);
		add(movesLeft);
	}
	
	private JLabel phase;
	private JLabel movesLeft;
}
