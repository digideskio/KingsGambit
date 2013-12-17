package kingsgambit.model.piece;

import kingsgambit.model.Direction;
import kingsgambit.model.Faction;
import kingsgambit.model.Square;
import kingsgambit.model.WeaponDieFace;

public class PieceFactory {

	public static final ScanPattern inFrontPattern = new ScanPattern(new boolean[][]{{true}, {false}}, new Square(1, 0));
	public static final ScanPattern archerPattern = new ScanPattern(new boolean[][]{{true, true, true}, {true, true, true},{true, true, true},}, new Square(3, 1));
	public static final ScanPattern adjacentPattern = new ScanPattern(new boolean[][]{{true, true, true}, {true, false, true},{true, true, true},}, new Square(1, 1));

	public static final Ability BASIC_MELEE = new Ability(inFrontPattern, 1, WeaponDieFace.AXE);
	public static final Ability DOUBLE_HIT_MELEE = new Ability(inFrontPattern, 2, WeaponDieFace.AXE);
	public static final Ability AXE_OR_BOW = new Ability(inFrontPattern, 1, WeaponDieFace.AXE, WeaponDieFace.BOW);
	public static final Ability RANGED = new Ability(archerPattern, 1, WeaponDieFace.BOW);
	
	public static Direction getDirection(Faction f) {
		return f == Faction.RED ? Direction.SOUTH : Direction.NORTH;
	}
	
	public static Piece getFootsoldier(Faction faction) {
		Piece soldier = new Piece("Soldier", faction, 4, 1, BASIC_MELEE);
		soldier.setFacing(getDirection(faction));
		
		return soldier;
	}

	public static Piece getKnight(Faction faction) {
		Piece knight =  new Piece("Knight", faction, 10, 2, 2, DOUBLE_HIT_MELEE);
		knight.setFacing(getDirection(faction));
		
		return knight;
	}

	public static Piece getArcher(Faction faction) {
		Piece archer = new Piece("Archer", faction, 4, 1, RANGED);
		archer.setFacing(getDirection(faction));
		
		return archer;
	}

	public static Piece getKing(Faction faction) {
		Piece king = new Piece("King", faction, 1, 2, DOUBLE_HIT_MELEE);
		king.addProperty(PieceProperty.FEARLESS);
		king.setFacing(getDirection(faction));

		return king;
	}

	public static Piece getMercenary(Faction faction) {
		Piece merc = new Piece("Mercenary", faction, 2, 1, BASIC_MELEE);
		merc.addProperty(PieceProperty.FEARLESS);
		merc.addProperty(PieceProperty.INTIMIDATING);
		merc.setFacing(getDirection(faction));
		
		return merc;
	}

	public static Piece getPeasant(Faction faction) {
		Piece peasant = new Piece("Peasant", faction, 4, 1, AXE_OR_BOW);
		peasant.addProperty(PieceProperty.FEARFUL);
		peasant.setFacing(getDirection(faction));
		
		return peasant;
	}

	public static Piece getLandsknecht(Faction faction) {
		Piece lands = new Piece("Landsknecht", faction, 2, 2, new Ability(adjacentPattern, 2, WeaponDieFace.AXE));
		lands.setMovesPerMove(2);
		lands.setMovesPerTurn(2);
		lands.setFacing(getDirection(faction));
		
		return lands;
	}
}
