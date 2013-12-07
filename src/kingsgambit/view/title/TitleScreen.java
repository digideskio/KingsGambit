package kingsgambit.view.title;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import kingsgambit.controller.BattleController;
import kingsgambit.model.BattleParameters;
import kingsgambit.model.PlayerControlOption;
import kingsgambit.view.battle.BattleView;

public class TitleScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String... args) {
		new TitleScreen();
	}
	
	public TitleScreen() {
		super("King's Gambit");
		
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(new JLabel("King's Gambit"));
		
		JButton newGame = new JButton("New Game");
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGameDialog();
			}
		});
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		getContentPane().add(newGame);
		getContentPane().add(exit);
		
		setSize(100, 200);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void newGameDialog() {
		final JFrame newGame = new JFrame("New Game");
		newGame.setLayout(new FlowLayout());
		newGame.setSize(200, 250);

		newGame.add(new JLabel("Red Player:"));
		final JComboBox<PlayerControlOption> redOptions = new JComboBox<PlayerControlOption>(
				PlayerControlOption.values()
		);
		newGame.add(redOptions);

		newGame.add(new JLabel("Blue Player:"));
		final JComboBox<PlayerControlOption> blueOptions = new JComboBox<PlayerControlOption>(
				new PlayerControlOption[] {PlayerControlOption.EASY}
		);
		newGame.add(blueOptions);

		JButton begin = new JButton("Begin");
		begin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame((PlayerControlOption)redOptions.getSelectedItem(), (PlayerControlOption)blueOptions.getSelectedItem());
			}
		});
		newGame.add(begin);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame.setVisible(false);
			}
		});
		newGame.add(cancel);

		newGame.setLocationRelativeTo(null);
		newGame.setVisible(true);
	}
	
	private void startGame(final PlayerControlOption red, final PlayerControlOption blue) {
		new Thread(
			new Runnable() {
				public void run() {
					BattleParameters parameters = new BattleParameters(red, blue);
					BattleController controller = new BattleController(parameters);
					BattleView view = new BattleView(controller);
					controller.setView(view);
					controller.getBattle().begin();
				}
			}
		).start();
	}
}
