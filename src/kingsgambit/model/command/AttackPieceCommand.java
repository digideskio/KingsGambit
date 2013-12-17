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
	
	/**
	 * Find the number of hits scored from this roll.
	 * Where the ability hits with multiple faces (as in the Peasant's attack),
	 * the greater of the faces hit is used.
	 */
	public int getHits(WeaponDieFace[] facesRolled) {
		int maxHits = 0;
		for (WeaponDieFace hitFace : facesForHits) {
			int hitsThisFace = 0;
			for (WeaponDieFace f : facesRolled) {
				if (f == hitFace)
					++hitsThisFace;
			}
			maxHits = Math.max(maxHits, hitsThisFace);
		}
		return maxHits;
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
