package kingsgambit.view.battle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import jmotion.AnimationFactory;
import jmotion.animation.FrameSet;
import kingsgambit.model.Faction;
import kingsgambit.model.battle.BattleConfiguration;
import kingsgambit.model.piece.Piece;

public class PlacePiecesPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public Piece getSelected() {
		return toPlace.getSelectedValue();
	}
	
	public boolean hasMorePieces() {
		return !model.isEmpty();
	}
	
	public void placePiece() {
		int selected = toPlace.getSelectedIndex();
		model.removeElementAt(selected);
		toPlace.setSelectedIndex(Math.max(0, Math.min(selected, model.size()-1)));
	}

	public PlacePiecesPanel(final BattleView view, BattleConfiguration config, final Faction faction) {
		setPreferredSize(new Dimension(200, 600));
		model = new DefaultListModel<>();
		if (faction == Faction.RED) {
			for (Piece p : config.getRedOptions())
				model.addElement(p);
		} else {
			for (Piece p : config.getBlueOptions())
				model.addElement(p);
		}

		toPlace = new JList<>(model);
		toPlace.setCellRenderer(new PieceRenderer());
		toPlace.setSelectedIndex(0);
		
		JScrollPane scroller = new JScrollPane(toPlace);
		scroller.setPreferredSize(new Dimension(200, 500));
		add(scroller);
		
		JButton doneButton = new JButton("Ready!");
		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.placementDone(faction);
			}
		});
		add(doneButton);
	}

	private class PieceRenderer implements ListCellRenderer<Piece> {
		public Component getListCellRendererComponent(JList<? extends Piece> list, Piece value, int index, boolean isSelected, boolean cellHasFocus) {
			FrameSet f = AnimationFactory.get(value.getType().toLowerCase());
			f.setSequence(8);
			
			JPanel panel = new JPanel();
			panel.setBackground(isSelected ? Color.GRAY : Color.WHITE);
			
			ImageIcon icon = new ImageIcon(f.currentFrame());
			panel.add(new JLabel(icon));
			panel.add(new JLabel(value.getType()));

			return panel;
		}
	}
	
	private JList<Piece> toPlace;
	private DefaultListModel<Piece> model;
}
