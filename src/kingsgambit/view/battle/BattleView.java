package kingsgambit.view.battle;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import jmotion.animation.Animation;
import jmotion.animation.AnimationSequence;
import jmotion.animation.FrameSet;
import kingsgambit.controller.BattleController;
import kingsgambit.model.Battle;
import kingsgambit.model.Faction;
import kingsgambit.model.Player;
import kingsgambit.model.Square;
import kingsgambit.model.command.Command;
import kingsgambit.model.event.AttackEvent;
import kingsgambit.model.event.BeginTurnEvent;
import kingsgambit.model.event.GameEvent;
import kingsgambit.model.piece.Piece;
import kingsgambit.view.Banner;
import kingsgambit.view.DiceView;

public class BattleView extends JFrame {
	private static final long serialVersionUID = 1L;

	public Animation animate(GameEvent event) {
		if (event instanceof AttackEvent) {
			final AttackEvent attack = (AttackEvent)event;
			
			Animation dice = diceView.roll(attack.roll);
			Animation startPreview = new Animation() {
				public void stepAhead(int millis) {
				}
				public void start() {
					boardView.highlight(attack.attacker.getPosition(), Color.RED);
					boardView.highlight(attack.defender.getPosition(), Color.RED);
				}
				public boolean isFinished() {
					return true;
				}
			};
			Animation endPreview = new Animation() {
				public void stepAhead(int millis) {
				}
				public void start() {
					boardView.clearHighlights();
				}
				public boolean isFinished() {
					return true;
				}
			};
			
			Animation a = new AnimationSequence(startPreview, dice, boardView.getAnimation(event), endPreview);
			boardView.addAnimation(a);
			return a;
		} else if (event instanceof BeginTurnEvent) {
			Animation banner;
			if (((BeginTurnEvent)event).faction == Faction.BLUE) {
				state = ENEMY_TURN;
				banner = enemyTurn.displayAndPlay();
			} else {
				state = UNSELECTED;
				banner = yourTurn.displayAndPlay();
			}
			boardView.addAnimation(banner);
			return banner;
		} else {
			Animation a = boardView.getAnimation(event);
			boardView.addAnimation(a);
			getWaitingState(a, state).enterState();
			return a;
		}
	}
	
	public void advanceFrame(int millis) {
		Point p = boardView.getMousePosition();		
		if (clicked)
			state.squareClicked(boardView.getSquareForPoint(p));
		clicked = false;
		
		state.tick();
	}
	
	public BattleView(BattleController controller) {
		this.controller = controller;
		battle = controller.getBattle();
		boardView = new BoardView(controller.getBattle().getBoard(), this);
		diceView = new DiceView();

		setSize(800, 800);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(boardView);
		getContentPane().add(diceView);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		state = UNSELECTED;
		
		boardView.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				clicked = true;
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
				clicked = true;
			}
		});
		
		try {
			BufferedImage yourTurnImg = ImageIO.read(new File("assets/your_turn.gif"));
			BufferedImage enemyTurnImg = ImageIO.read(new File("assets/enemy_turn.gif"));

			FrameSet yourTurnFrames = new FrameSet(300, 100);
			FrameSet enemyTurnFrames = new FrameSet(300, 100);
			for (int i = 0; i<40; ++i) {
				yourTurnFrames.addFrame(0, yourTurnImg);
				enemyTurnFrames.addFrame(0, enemyTurnImg);
			}

			yourTurn = new Banner(yourTurnFrames, boardView.getBannerLayer());
			yourTurn.setLocation((boardView.getWidth()-yourTurnFrames.getWidth())/2, 
					(boardView.getHeight()-yourTurnFrames.getHeight())/2);
			enemyTurn = new Banner(enemyTurnFrames, boardView.getBannerLayer());
			enemyTurn.setLocation(yourTurn.getX(), yourTurn.getY());
		} catch (IOException io) {
		}
		
		boardView.startAnimating();
	}

	private abstract class ViewState {
		public abstract void enterState();
		public abstract void squareClicked(Square s);
		public void tick() {
			
		}
	}
	
	private final ViewState ENEMY_TURN = new ViewState() {
		public void enterState() {
			boardView.selectPiece(null);
			boardView.clearPreviews();
			boardView.clearHighlights();
		}

		public void squareClicked(Square s) {
		}
	};

	private final ViewState ENEMY_SELECTED = new ViewState() {
		public void enterState() {
			System.out.println("entering ENEMY_SELECTED");
			boardView.clearHighlights();
			boardView.clearPreviews();
			state = this;
			boardView.selectPiece(selectedPiece);

			for (Command c : selectedPiece.getCommands())
				boardView.previewCommand(c);
		}
		
		public void squareClicked(Square s) {
			if (controller.getBattle().getBoard().hasPieceAt(s)) {
				Piece clickedPiece = controller.getBattle().getBoard().getPieceAt(s);
				if (clickedPiece == selectedPiece) {
					UNSELECTED.enterState();
				} else {
					selectedPiece = clickedPiece; 
					if (selectedPiece.getFaction() == Faction.RED)
						SELECTED.enterState();
					else
						ENEMY_SELECTED.enterState();
				}
			}
		}
	};
	
	private final ViewState UNSELECTED = new ViewState() {
		public void enterState() {
			state = this;
			selectedPiece = null;
			boardView.selectPiece(null);
			boardView.clearPreviews();
			boardView.clearHighlights();
		}
		public void squareClicked(Square s) {
			if (controller.getBattle().getBoard().hasPieceAt(s)) {
				selectedPiece = controller.getBattle().getBoard().getPieceAt(s);
				if (selectedPiece.getFaction() == Faction.RED)
					SELECTED.enterState();
				else
					ENEMY_SELECTED.enterState();
			}
		}
	};
	
	private final ViewState SELECTED = new ViewState() {
		public void enterState() {
			boardView.clearHighlights();
			boardView.clearPreviews();
			state = this;
			boardView.selectPiece(selectedPiece);

			Player player = battle.getPlayer(selectedPiece.getFaction());
			for (Command c : player.getLegalCommands(selectedPiece))
				boardView.previewCommand(c);
		}
		public void squareClicked(Square s) {
			Command command = null;
			Player player = battle.getPlayer(selectedPiece.getFaction());
			for (Command c : player.getLegalCommands(selectedPiece)) {
				if (c.getOperativeSquare().equals(s))
					command = c;
			}
			if (command != null) {
				controller.executeCommand(command);
				UNSELECTED.enterState();
			} else {
				if (battle.getBoard().hasPieceAt(s)) {
					Piece clicked = battle.getBoard().getPieceAt(s);
					if (clicked == selectedPiece) {
						UNSELECTED.enterState();
					} else {
						selectedPiece = clicked;	
						if (clicked.getFaction() == Faction.RED)
							SELECTED.enterState();
						else
							ENEMY_SELECTED.enterState();
					}
				}
			}
		}
	};
	
	private ViewState getWaitingState(final Animation a, final ViewState transitionTo) {
		return new ViewState() {
			public void enterState() {
				state = this;
			}

			public void squareClicked(Square s) {
			}
			
			public void tick() {
				if (a.isFinished())
					transitionTo.enterState();
			}
		};
	}
	
	private Piece selectedPiece;
	
	private ViewState state;
	private boolean clicked;
	private BattleController controller;
	private Battle battle;
	private BoardView boardView;
	private DiceView diceView;
	
	private Banner yourTurn;
	private Banner enemyTurn;
}
