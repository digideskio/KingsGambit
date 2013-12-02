package kingsgambit.model.command;

import kingsgambit.model.Square;
import kingsgambit.model.WeaponDieFace;
import kingsgambit.model.piece.Piece;

public class AttackPieceCommand implements Command {
	public Square getOperativeSquare() {
		return defender.getPosition();
	}
	
	public void accept(CommandExecutor cex) {
		cex.execute(this);
	}

	public int getCost() {
		return cost;
	}

	public String toString() {
		return attacker + " attacks " + defender;
	}
	
	public int getHits(WeaponDieFace[] faces) {
		int hits = 0;
		for (WeaponDieFace f : faces) {
			for (WeaponDieFace hitFace : facesForHits) {
				if (f == hitFace)
					++hits;
			}
		}
		return hits;
	}

	public final int numRolls;
	public final Piece attacker;
	public final Piece defender;
	
	public AttackPieceCommand(Piece attacker, Piece defender, int numRolls, WeaponDieFace[] facesForHits, int cost) {
		this.attacker = attacker;
		this.defender = defender;
		this.numRolls = numRolls;
		this.facesForHits = facesForHits;
		this.cost = cost;
	}
	
	private int cost;
	private WeaponDieFace[] facesForHits;
}
