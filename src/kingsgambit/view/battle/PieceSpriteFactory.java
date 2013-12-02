package kingsgambit.view.battle;

import jmotion.sprite.SpriteSpace;
import kingsgambit.model.piece.Piece;

public class PieceSpriteFactory {
	public static PieceSprite createSprite(Piece piece, BoardView view, SpriteSpace space) {
		if (piece.getNumFigures() == 1)
			return new SinglePieceSprite(piece, view, space);
		
		return new GroupPieceSprite(piece, view, space);
	}
}
