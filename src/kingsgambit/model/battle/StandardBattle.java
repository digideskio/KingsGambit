package kingsgambit.model.battle;

import java.util.LinkedList;

import kingsgambit.model.Faction;
import kingsgambit.model.Square;
import kingsgambit.model.piece.Piece;
import kingsgambit.model.piece.PieceFactory;

public class StandardBattle extends BattleType {

	public BattleConfiguration createConfiguration() {
		LinkedList<Piece> initial = new LinkedList<Piece>();
		
		// Kings
		Piece redKing = PieceFactory.getKing(Faction.RED);
		redKing.setPosition(new Square(0, 4));
		initial.add(redKing);

		Piece blueKing = PieceFactory.getKing(Faction.BLUE);
		blueKing.setPosition(new Square(7, 4));
		initial.add(blueKing);
		
		// Soldiers
		for (int r = 2; r<7; ++r) {
			Piece redSoldier = PieceFactory.getFootsoldier(Faction.RED);
			redSoldier.setPosition(new Square(1, r));
			initial.add(redSoldier);
			
			Piece blueSoldier = PieceFactory.getFootsoldier(Faction.BLUE);
			blueSoldier.setPosition(new Square(6, r));
			initial.add(blueSoldier);
		}

		// Knights
		Piece redKnight = PieceFactory.getKnight(Faction.RED);
		redKnight.setPosition(new Square(0, 3));
		initial.add(redKnight);

		redKnight = PieceFactory.getKnight(Faction.RED);
		redKnight.setPosition(new Square(0, 5));
		initial.add(redKnight);
		
		Piece blueKnight = PieceFactory.getKnight(Faction.BLUE);
		blueKnight.setPosition(new Square(7, 3));
		initial.add(blueKnight);
		
		blueKnight = PieceFactory.getKnight(Faction.BLUE);
		blueKnight.setPosition(new Square(7, 5));
		initial.add(blueKnight);

		// Archers
		Piece blueArcher = PieceFactory.getArcher(Faction.BLUE);
		blueArcher.setPosition(new Square(7, 2));
		initial.add(blueArcher);
		
		blueArcher = PieceFactory.getArcher(Faction.BLUE);
		blueArcher.setPosition(new Square(7, 6));
		initial.add(blueArcher);

		Piece redArcher = PieceFactory.getArcher(Faction.RED);
		redArcher.setPosition(new Square(0, 2));
		initial.add(redArcher);
		
		redArcher = PieceFactory.getArcher(Faction.RED);
		redArcher.setPosition(new Square(0, 6));
		initial.add(redArcher);
		
		return new BattleConfiguration(initial, new LinkedList<Piece>(), new LinkedList<Piece>());
	}
	
	public StandardBattle() {
		super("Stanard Battle", "Fight against an opponent with basic army");
	}
}
