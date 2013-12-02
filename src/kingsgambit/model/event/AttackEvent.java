package kingsgambit.model.event;

import kingsgambit.model.WeaponDieFace;
import kingsgambit.model.piece.Piece;

public class AttackEvent implements GameEvent {
	public final Piece attacker;
	public final Piece defender;
	public final int numKills;
	public final WeaponDieFace[] roll;

	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}
	
	public String toString() {
		return "AttackEvent("+attacker+", "+defender+")";
	}
	
	public AttackEvent(Piece attacker, Piece defender, int numKills, WeaponDieFace[] roll) {
		this.attacker = attacker;
		this.defender = defender;
		this.numKills = numKills;
		this.roll = roll;
	}
}
