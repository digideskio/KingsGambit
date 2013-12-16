package kingsgambit.model.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kingsgambit.controller.BattleController;
import kingsgambit.model.Player;
import kingsgambit.model.Square;
import kingsgambit.model.battle.Board;
import kingsgambit.model.command.AttackPieceCommand;
import kingsgambit.model.command.Command;
import kingsgambit.model.command.MovePieceCommand;
import kingsgambit.model.command.TurnPieceCommand;
import kingsgambit.model.piece.Piece;

public class SimpleAI extends BaseAI {

	protected Command bestMove() {
		Iterable<Piece> enemyPieces = controller.getBattle().getBoard()
				.getPieces(controller.getBattle().getEnemy(player).faction);
		
		Board board = controller.getBattle().getBoard();
		enemyDistances = new int[board.getColumns()][board.getRows()];
		for (int c = 0; c<board.getColumns(); ++c) {
			enemyDistances[c] = new int[board.getRows()];
			for (int r = 0; r<board.getRows(); ++r) {
				for (Piece p : enemyPieces)
					enemyDistances[c][r] = Math.abs(c - p.getPosition().column) + Math.abs(r - p.getPosition().row);
			}
		}
		
		ArrayList<Command> allCommands = new ArrayList<Command>();
		allCommands.addAll(player.getLegalCommands());
	
		Collections.shuffle(allCommands);
		Collections.sort(allCommands, new Comparator<Command>() {
			public int compare(Command a, Command b) {
				return score(a) - score(b);
			}
		});
		
		return allCommands.get(0);
	}
	
	private int score(Command c) {
		if (c instanceof AttackPieceCommand)
			return -100;
		
		if (c instanceof TurnPieceCommand) {
			TurnPieceCommand turn = (TurnPieceCommand)c;
			Piece mover = turn.getMover();
			// Turns directly toward an enemy?
			Square newFaces = turn.getFacing().projectFrom(mover.getPosition(), 1);
			if (controller.getBattle().containsEnemy(newFaces, player))
				return -30;
			
			// Turns closer toward an enemy?
			int initialDist = 0;
			boolean facesEnemy = false;
			for (Square s : controller.getBattle().getBoard().line(mover.getPosition(), turn.getMover().getFacing())) {
				if (controller.getBattle().containsEnemy(s, player)) {
					facesEnemy = true;
					break;
				}
				++initialDist;
			}
			if (!facesEnemy)
				initialDist = 1000;
			
			int turnDist = 0;
			facesEnemy = false;
			for (Square s : controller.getBattle().getBoard().line(mover.getPosition(), turn.getFacing())) {
				if (controller.getBattle().containsEnemy(s, player)) {
					facesEnemy = true;
					break;
				}
				++turnDist;
			}
			if (!facesEnemy)
				turnDist = 1000;
			
			if (turnDist < initialDist)
				return -20;
			
			return 0;
		} else if (c instanceof MovePieceCommand) {
			MovePieceCommand move = (MovePieceCommand)c;
			
			// Move directly into attack?
			//
			
			// Moves closer to the enemy?
			Square from = move.getMover().getPosition();
			Square to = move.getDestination();
			if (enemyDistances[to.column][to.row] < enemyDistances[from.column][from.row])
				return -10;
		}
		
		return 0;
	}
	
	public SimpleAI(BattleController controller, Player player) {
		super(controller, player);
	}
	
	private int[][] enemyDistances;
}
