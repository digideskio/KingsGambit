package kingsgambit.model.piece;

import kingsgambit.model.Faction;
import kingsgambit.model.Square;
import kingsgambit.model.WeaponDieFace;

public class PieceFactory {

	public static final ScanPattern inFrontPattern = new ScanPattern(new boolean[][]{{true}, {false}}, new Square(1, 0));
	public static final ScanPattern archerPattern = new ScanPattern(new boolean[][]{{true, true, true}, {true, true, true},{true, true, true},}, new Square(3, 1));
	public static final ScanPattern adjacentPattern = new ScanPattern(new boolean[][]{{true, true, true}, {true, false, true},{true, true, true},}, new Square(1, 1));

	public static final Ability BASIC_MELEE = new Ability(inFrontPattern, 1, WeaponDieFace.AXE);
	public static final Ability DOUBLE_HIT_MELEE = new Ability(inFrontPattern, 2, WeaponDieFace.AXE);
	public static final Ability RANGED = new Ability(archerPattern, 1, WeaponDieFace.BOW);
	
	public static Piece getFootsoldier(Faction faction) {
		return new Piece("Soldier", faction, 4, 1, BASIC_MELEE);
	}

	public static Piece getKnight(Faction faction) {
		return new Piece("Knight", faction, 10, 2, 2, DOUBLE_HIT_MELEE);
	}

	public static Piece getArcher(Faction faction) {
		Piece archer = new Piece("Archer", faction, 4, 1, RANGED);
		return archer;
	}

	public static Piece getKing(Faction faction) {
		Piece king = new Piece("King", faction, 1, 2, DOUBLE_HIT_MELEE);
		king.addProperty(PieceProperty.FEARLESS);
		return king;
	}
}
