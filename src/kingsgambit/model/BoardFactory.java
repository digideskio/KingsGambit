package kingsgambit.model;

import kingsgambit.model.piece.Piece;
import kingsgambit.model.piece.PieceFactory;

public class BoardFactory {

	public static Board getBoard() {
		Board b = new Board(9, 8);
		
		// Kings
		Piece redKing = PieceFactory.getKing(Faction.RED);
		redKing.setPosition(new Square(0, 4));
		redKing.setFacing(Direction.SOUTH);
		b.addPiece(redKing);

		Piece blueKing = PieceFactory.getKing(Faction.BLUE);
		blueKing.setPosition(new Square(7, 4));
		blueKing.setFacing(Direction.NORTH);
		b.addPiece(blueKing);
		
		// Soldiers
		for (int r = 2; r<7; ++r) {
			Piece redSoldier = PieceFactory.getFootsoldier(Faction.RED);
			redSoldier.setPosition(new Square(1, r));
			redSoldier.setFacing(Direction.SOUTH);
			b.addPiece(redSoldier);
			
			Piece blueSoldier = PieceFactory.getFootsoldier(Faction.BLUE);
			blueSoldier.setPosition(new Square(6, r));
			blueSoldier.setFacing(Direction.NORTH);
			b.addPiece(blueSoldier);
		}

		// Knights
		Piece redKnight = PieceFactory.getKnight(Faction.RED);
		redKnight.setPosition(new Square(0, 3));
		redKnight.setFacing(Direction.SOUTH);
		b.addPiece(redKnight);

		redKnight = PieceFactory.getKnight(Faction.RED);
		redKnight.setPosition(new Square(0, 5));
		redKnight.setFacing(Direction.SOUTH);
		b.addPiece(redKnight);
		
		Piece blueKnight = PieceFactory.getKnight(Faction.BLUE);
		blueKnight.setPosition(new Square(7, 3));
		blueKnight.setFacing(Direction.NORTH);
		b.addPiece(blueKnight);
		
		blueKnight = PieceFactory.getKnight(Faction.BLUE);
		blueKnight.setPosition(new Square(7, 5));
		blueKnight.setFacing(Direction.NORTH);
		b.addPiece(blueKnight);

		// Archers
		Piece blueArcher = PieceFactory.getArcher(Faction.BLUE);
		blueArcher.setPosition(new Square(7, 2));
		blueArcher.setFacing(Direction.NORTH);
		b.addPiece(blueArcher);
		
		blueArcher = PieceFactory.getArcher(Faction.BLUE);
		blueArcher.setPosition(new Square(7, 6));
		blueArcher.setFacing(Direction.NORTH);
		b.addPiece(blueArcher);

		Piece redArcher = PieceFactory.getArcher(Faction.RED);
		redArcher.setPosition(new Square(0, 2));
		redArcher.setFacing(Direction.SOUTH);
		b.addPiece(redArcher);
		
		redArcher = PieceFactory.getArcher(Faction.RED);
		redArcher.setPosition(new Square(0, 6));
		redArcher.setFacing(Direction.SOUTH);
		b.addPiece(redArcher);

		return b;
	}
}
