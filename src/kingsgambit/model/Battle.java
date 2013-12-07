package kingsgambit.model;

import kingsgambit.controller.BattleController;
import kingsgambit.model.command.AttackPieceCommand;
import kingsgambit.model.command.Command;
import kingsgambit.model.command.CommandExecutor;
import kingsgambit.model.command.EndTurnCommand;
import kingsgambit.model.command.MovePieceCommand;
import kingsgambit.model.command.TurnPieceCommand;
import kingsgambit.model.event.AttackEvent;
import kingsgambit.model.event.BeginTurnEvent;
import kingsgambit.model.event.DieEvent;
import kingsgambit.model.event.PieceMoveEvent;
import kingsgambit.model.event.PieceTurnEvent;
import kingsgambit.model.event.RetreatEvent;
import kingsgambit.model.piece.Piece;
import kingsgambit.model.piece.PieceProperty;

public class Battle implements CommandExecutor {

	@Override
	public void executeCommand(Command c) {
		System.out.println("Executing " + c);
		c.accept(this);
		
		movingPlayer.spendMoves(c.getCost());
		
		// Check for end of turn due to spending all moves allowed
		if (!(c instanceof EndTurnCommand) && movingPlayer.getMovesLeft() == 0)
			endTurn();
	}
	
	@Override
	public void execute(EndTurnCommand end) {
		endTurn();
	}

	@Override
	public void execute(MovePieceCommand c) {
		board.movePiece(c.getMover(), c.getDestination());
		controller.handle(new PieceMoveEvent(c.getMover(), c.getSource(), c.getDestination()));
	}

	@Override
	public void execute(TurnPieceCommand c) {
		c.getMover().setFacing(c.getFacing());
		controller.handle(new PieceTurnEvent(c.getMover(), c.getFacing()));
	}

	@Override
	public void execute(AttackPieceCommand c) {
		c.attacker.setHasActed(true);
		
		WeaponDieFace[] roll = weaponDice.roll(c.numRolls);
		int numKills = c.defender.takeHits(c.getHits(roll));
		
		if (c.defender.isDead())
			board.removePiece(c.defender);
		
		// Publicize the event
		controller.handle(new AttackEvent(c.attacker, c.defender, numKills, roll));

		// Check for attacker retreat
		if (!c.attacker.hasProperty(PieceProperty.FEARLESS)) {
			int numRetreatRolls = 0;
			for (WeaponDieFace f : roll) {
				if (f == WeaponDieFace.RETREAT)
					++numRetreatRolls;
			}
			if (c.attacker.hasProperty(PieceProperty.FEARFUL) && numRetreatRolls > 0)
				causeRetreat(c.attacker, c.attacker.getFacing().aboutFace(), numRetreatRolls); // Fearful retreat, one square per roll
			else if (numRetreatRolls == c.numRolls)
				causeRetreat(c.attacker, c.attacker.getFacing().aboutFace(), 1);
		}
	}
	
	public Board getBoard() {
		return board;
	}

	public Player getRed() {
		return red;
	}

	public Player getBlue() {
		return blue;
	}

	/**
	 * Does the square given contain a piece that belongs to the enemy of player p
	 */
	public boolean containsEnemy(Square s, Player p) {
		return board.hasPieceAt(s) && board.getPieceAt(s).getFaction() != p.faction;
	}
	
	public Player getEnemy(Player p) {
		return p == red ? blue : red;
	}
	
	public Player getPlayer(Faction f) {
		return f == Faction.RED ? red : blue;
	}
	
	public boolean isGameOver() {
		return redKing.isDead() || blueKing.isDead();
	}
	
	public Player getVictor() {
		return redKing.isDead() ? blue : red;
	}
	
	public DiceRoller getWeaponDice() {
		return weaponDice;
	}

	public void setController(BattleController controller) {
		this.controller = controller;
	}
	
	public void begin() {
		movingPlayer = red;
		controller.handle(new BeginTurnEvent(Faction.RED));
	}
	
	public Battle(BattleParameters parameters) {
		board = BoardFactory.getBoard();
		
		red = new Player(Faction.RED, board, 2);
		blue = new Player(Faction.BLUE, board, 2);
		movingPlayer = red;

		weaponDice = new DiceRoller(WeaponDieFace.AXE, WeaponDieFace.AXE, WeaponDieFace.AXE, WeaponDieFace.BOW, WeaponDieFace.BOW, WeaponDieFace.RETREAT);
		
		for (Piece p : board.getPieces(Faction.RED)) {
			if (p.getType().equals("King"))
				redKing = p;
		}

		for (Piece p : board.getPieces(Faction.BLUE)) {
			if (p.getType().equals("King"))
				blueKing = p;
		}
	}
	
	private void endTurn() {
		for (Piece piece : board.getPieces(movingPlayer.faction))
			piece.setHasActed(false);
		movingPlayer = movingPlayer == red ? blue : red;
		movingPlayer.resetTurn();

		controller.handle(new BeginTurnEvent(movingPlayer.faction));
	}
	
	private void causeRetreat(Piece piece, Direction direction, int distance) {
		System.out.println(piece + " will retreat " + distance);
		// Turn piece around
		
		piece.setFacing(direction);

		for (int i = 1; i <= distance; ++i) {
			Square retreatSquare = direction.projectFrom(piece.getPosition(), i);
		
			// Apply cascading retreat.
			if (board.hasPieceAt(retreatSquare)) {
				Piece inTheWay = board.getPieceAt(retreatSquare);
				
				// A different colored piece or a Fearless piece will not retreat, instead the retreating piece dies.
				if (inTheWay.hasProperty(PieceProperty.FEARLESS) || inTheWay.getFaction() != piece.getFaction()) {
					piece.die();
					board.removePiece(piece);
					controller.handle(new DieEvent(piece));
					return;
				} else {
					causeRetreat(inTheWay, direction, distance-i+1);
				}
			}
			
			// Retreating off the board kills the piece.
			if (!board.containsSquare(retreatSquare)) {
				if (i > 1) {
					Square retreatFrom = piece.getPosition();
					Square retreatTo = direction.projectFrom(piece.getPosition(), i-1);
					board.movePiece(piece, retreatTo);
					controller.handle(new RetreatEvent(piece, retreatFrom, retreatTo));
				}
				piece.die();
				board.removePiece(piece);
				controller.handle(new DieEvent(piece));
				return;
			}
		}
		
		// "Advance" the piece in the retreat direction.
		Square retreatFrom = piece.getPosition();
		Square retreatTo = direction.projectFrom(piece.getPosition(), distance);
		board.movePiece(piece, retreatTo);
		controller.handle(new RetreatEvent(piece, retreatFrom, retreatTo));
	}

	private BattleController controller;
	
	private DiceRoller weaponDice;
	
	private Player movingPlayer;
	private Player red;
	private Player blue;
	
	private Piece redKing;
	private Piece blueKing;
	
	private Board board;
}
