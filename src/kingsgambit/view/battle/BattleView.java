package kingsgambit.view.battle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jmotion.animation.Animation;
import jmotion.animation.AnimationSequence;
import jmotion.animation.FrameSet;
import jmotion.construction.SpriteLoader;
import kingsgambit.controller.BattleController;
import kingsgambit.model.Faction;
import kingsgambit.model.Player;
import kingsgambit.model.Square;
import kingsgambit.model.battle.Battle;
import kingsgambit.model.battle.BattleConfiguration;
import kingsgambit.model.battle.Board;
import kingsgambit.model.battle.BoardRegion;
import kingsgambit.model.command.Command;
import kingsgambit.model.command.FactionReadyCommand;
import kingsgambit.model.command.PlacePieceCommand;
import kingsgambit.model.command.UnplacePieceCommand;
import kingsgambit.model.event.AttackEvent;
import kingsgambit.model.event.BattleBeginEvent;
import kingsgambit.model.event.BeginTurnEvent;
import kingsgambit.model.event.GameEvent;
import kingsgambit.model.event.GameOverEvent;
import kingsgambit.model.piece.Piece;
import kingsgambit.view.Banner;
import kingsgambit.view.DiceView;

public class BattleView extends JFrame {
	public static final SpriteLoader LOADER = new SpriteLoader("assets");
	
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
			BeginTurnEvent beginTurn = (BeginTurnEvent)event;
			turnDetails.setTurn(beginTurn.faction);
			
			// Display a banner indicating whos turn it is
			Animation banner;
			if (beginTurn.faction == Faction.BLUE) {
				state = controlBlue ? UNSELECTED : ENEMY_TURN;
				banner = blueTurn.displayAndPlay();
			} else {
				state = controlRed ? UNSELECTED : ENEMY_TURN;
				banner = redTurn.displayAndPlay();
			}
			boardView.addAnimation(banner);
			return banner;
		} else if (event instanceof BattleBeginEvent) {
			// The battle is starting, we must ready each player controlled by this view
			if (!battle.getConfiguration().requiresPlacementPhase()) {
				System.out.println(controlBlue);
				if (controlRed)
					controller.executeCommand(new FactionReadyCommand(Faction.RED));
				if (controlBlue)
					controller.executeCommand(new FactionReadyCommand(Faction.BLUE));
			}
			return null;
		} else if (event instanceof GameOverEvent) {
			state = ENEMY_TURN;
			return null;
		} else {
			Animation a = boardView.getAnimation(event);
			if (a != null) {
				boardView.addAnimation(a);
				getWaitingState(a, state).enterState();
			}
			return a;
		}
	}
	
	public void advanceFrame(int millis) {
		Point p = boardView.getMousePosition();		
		if (clicked) {
			System.out.println("Clicked " + state);
			state.squareClicked(boardView.getSquareForPoint(p));
		}
		clicked = false;
		
		state.tick();
	}
	
	public void placementDone(Faction f) {		
		// Do we need to place for next human player?
		if (f == Faction.RED && controlBlue) {
			remove(placePieces);
			preparePlacement(Faction.BLUE);
		} else {
			beginGame();
		}
		controller.executeCommand(new FactionReadyCommand(f));
	}

	public BattleView(BattleController controller, boolean controlRed, boolean controlBlue) {
		this.controller = controller;
		this.controlBlue = controlBlue;
		this.controlRed = controlRed;
		battle = controller.getBattle();
		
		boardView = new BoardView(controller.getBattle().getBoard(), this);
		JPanel boardFrame = new JPanel();
		boardFrame.add(boardView);
		diceView = new DiceView();
		turnDetails = new TurnDetails();
		
		state = ENEMY_TURN;

		setSize(900, 800);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(boardFrame, BorderLayout.CENTER);
		getContentPane().add(turnDetails, BorderLayout.NORTH);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		
		BufferedImage yourTurnImg = BattleView.LOADER.readImage("your_turn.gif");
		BufferedImage enemyTurnImg = BattleView.LOADER.readImage("enemy_turn.gif");

		FrameSet yourTurnFrames = new FrameSet(300, 100);
		FrameSet enemyTurnFrames = new FrameSet(300, 100);
		for (int i = 0; i<40; ++i) {
			yourTurnFrames.addFrame(0, yourTurnImg);
			enemyTurnFrames.addFrame(0, enemyTurnImg);
		}

		redTurn = new Banner(yourTurnFrames, boardView.getBannerLayer());
		redTurn.setLocation((boardView.getWidth()-yourTurnFrames.getWidth())/2, 
				(boardView.getHeight()-yourTurnFrames.getHeight())/2);
		blueTurn = new Banner(enemyTurnFrames, boardView.getBannerLayer());
		blueTurn.setLocation(redTurn.getX(), redTurn.getY());
		
		if (battle.getConfiguration().requiresPlacementPhase()) {
			// If this battle configuration requires pieces to be placed,
			// prepare to receive piece placements for the first player this view controls
			if (controlRed)
				preparePlacement(Faction.RED);
			else if (controlBlue)
				preparePlacement(Faction.BLUE);
			else
				beginGame(); // AI vs AI
		} else {
			beginGame();
		}
		
		boardView.startAnimating();
	}
	
	private void preparePlacement(Faction f) {
		BattleConfiguration config = controller.getBattle().getConfiguration();
		boardView.setShroud(config.getRegion(f).getComplement());
		
		placingFor = f;
		placePieces = new PlacePiecesPanel(this, config, f);
		getContentPane().add(placePieces, BorderLayout.EAST);
		getContentPane().revalidate();
		state = PLACE_PIECE;
	}
	
	private void beginGame() {
		boardView.setShroud(BoardRegion.getFullRegion().getComplement());
		System.out.println("BATTLE VIEW ADDING DICE");
		if (placePieces != null)
			getContentPane().remove(placePieces);
		
		getContentPane().add(diceView, BorderLayout.EAST);
		state = ENEMY_TURN;
		getContentPane().revalidate();
	}

	private abstract class ViewState {
		public abstract void enterState();
		public abstract void squareClicked(Square s);
		public void tick() {
			
		}
	}
	
	private final ViewState ENEMY_TURN = new ViewState() {
		public String toString() {
			return "ENEMY_TURN";
		}
		public void enterState() {
			state = this;
			boardView.selectPiece(null);
			boardView.clearPreviews();
			boardView.clearHighlights();
		}

		public void squareClicked(Square s) {
		}
	};

	private final ViewState ENEMY_SELECTED = new ViewState() {
		public String toString() {
			return "ENEMY_SELECTED";
		}
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
					if (clickedPiece.getFaction() == selectedPiece.getFaction()) {
						// same faction, still the enemy
						selectedPiece = clickedPiece;
						enterState();
					} else {
						// opposite faction, must be the player
						selectedPiece = clickedPiece;
						SELECTED.enterState();
					}
				}
			}
		}
	};
	
	private final ViewState UNSELECTED = new ViewState() {
		public String toString() {
			return "UNSELECTED";
		}
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
				Faction movingFaction = controller.getBattle().getMovingPlayer().faction;
				if (selectedPiece.getFaction() == movingFaction)
					SELECTED.enterState();
				else
					ENEMY_SELECTED.enterState();
			}
		}
	};
	
	private final ViewState SELECTED = new ViewState() {
		public String toString() {
			return "SELECTED";
		}
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
						Faction movingFaction = controller.getBattle().getMovingPlayer().faction;
						if (clicked.getFaction() == movingFaction)
							SELECTED.enterState();
						else
							ENEMY_SELECTED.enterState();
					}
				}
			}
		}
	};
	
	private final ViewState PLACE_PIECE = new ViewState() {
		public String toString() {
			return "PLACE_PIECE";
		}
		
		public void squareClicked(Square s) {
			BattleConfiguration config = battle.getConfiguration();
			Board board = battle.getBoard();
			
			if (!config.getRegion(placingFor).contains(s) || !board.containsSquare(s))
				return;
			
			if (board.hasPieceAt(s)) {
				Piece clicked = board.getPieceAt(s);
				if (!clicked.getType().equals("King")) {
					// TODO - this logic should check if the piece was in the initial conditions.
					placePieces.unplacePiece(clicked);
					controller.executeCommand(new UnplacePieceCommand(clicked, s));
				}
			} else if (placePieces.hasMorePieces()) {
				Piece piece = placePieces.getSelected();
				placePieces.placePiece();
				controller.executeCommand(new PlacePieceCommand(piece, s));
			}
		}
		
		public void enterState() {
			state = this;
		}
	};
	
	private ViewState getWaitingState(final Animation a, final ViewState transitionTo) {
		return new ViewState() {
			public String toString() {
				return "TRANSITION(" + a + ", " + transitionTo + ")";
			}
			public void enterState() {
				System.out.println("transitioning to " + transitionTo);
				state = this;
				if (a == null)
					transitionTo.enterState();
			}

			public void squareClicked(Square s) {
			}
			
			public void tick() {
				if (a == null || a.isFinished())
					transitionTo.enterState();
			}
		};
	} 
	
	private boolean controlRed;
	private boolean controlBlue;
	private Faction placingFor;
	
	private Piece selectedPiece;
	
	private ViewState state;
	private boolean clicked;
	private BattleController controller;
	private Battle battle;
	private BoardView boardView;
	private DiceView diceView;
	private PlacePiecesPanel placePieces;
	private TurnDetails turnDetails;
	
	private Banner redTurn;
	private Banner blueTurn;
}
