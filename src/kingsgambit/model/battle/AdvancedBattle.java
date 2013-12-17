package kingsgambit.model.battle;

import java.util.LinkedList;

import kingsgambit.model.Direction;
import kingsgambit.model.Faction;
import kingsgambit.model.Square;
import kingsgambit.model.piece.Piece;
import kingsgambit.model.piece.PieceFactory;

public class AdvancedBattle extends BattleType {

	public BattleConfiguration createConfiguration() {
		LinkedList<Piece> initial = new LinkedList<Piece>();

		BoardRegion redRegion = BoardRegion.createRectangle(0, 2, 5, 2);
		BoardRegion blueRegion = BoardRegion.createRectangle(6, 2, 5, 2);

		// Kings
		Piece redKing = PieceFactory.getKing(Faction.RED);
		redKing.setPosition(new Square(0, 4));
		redKing.setFacing(Direction.SOUTH);
		initial.add(redKing);

		Piece blueKing = PieceFactory.getKing(Faction.BLUE);
		blueKing.setPosition(new Square(7, 4));
		blueKing.setFacing(Direction.NORTH);
		initial.add(blueKing);

		LinkedList<Piece> red = new LinkedList<>();
		LinkedList<Piece> blue = new LinkedList<>();
		// Footsoldiers
		for (int i = 0; i<5; ++i) {
			red.add(PieceFactory.getFootsoldier(Faction.RED));
			blue.add(PieceFactory.getFootsoldier(Faction.BLUE));
		}
		// Archers
		for (int i = 0; i<3; ++i) {
			red.add(PieceFactory.getArcher(Faction.RED));
			blue.add(PieceFactory.getArcher(Faction.BLUE));
		}
		// Knights
		for (int i = 0; i<3; ++i) {
			red.add(PieceFactory.getKnight(Faction.RED));
			blue.add(PieceFactory.getKnight(Faction.BLUE));
		}
		// Mercs
		for (int i = 0; i<2; ++i) {
			red.add(PieceFactory.getMercenary(Faction.RED));
			blue.add(PieceFactory.getMercenary(Faction.BLUE));
		}
		// Peasants
		for (int i = 0; i<2; ++i) {
			red.add(PieceFactory.getPeasant(Faction.RED));
			blue.add(PieceFactory.getPeasant(Faction.BLUE));
		}
		// Landsknecht
		for (int i = 0; i<2; ++i) {
			red.add(PieceFactory.getLandsknecht(Faction.RED));
			blue.add(PieceFactory.getLandsknecht(Faction.BLUE));
		}
		
		return new BattleConfiguration(initial, red, redRegion, blue, blueRegion);
	}
	
	public AdvancedBattle() {
		super("Advanced Battle", "Fight against an opponent with basic army");
	}
}
