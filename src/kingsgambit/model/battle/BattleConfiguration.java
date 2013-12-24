package kingsgambit.model.battle;

import java.util.List;

import kingsgambit.model.Faction;
import kingsgambit.model.piece.Piece;

public class BattleConfiguration {
	public boolean requiresPlacementPhase() {
		return (redOptions != null && !redOptions.isEmpty()) || (blueOptions != null && !blueOptions.isEmpty());
	}
	
	public Iterable<Piece> getInitialPieces() {
		return initial;
	}
	
	public List<Piece> getOptions(Faction f) {
		if (f == Faction.RED)
			return redOptions;
		return blueOptions;
	}
	
	public BoardRegion getRegion(Faction f) {
		if (f == Faction.RED)
			return redPlacementRegion;
		return bluePlacementRegion;
	}
	
	public BattleConfiguration(List<Piece> initial, List<Piece> red, BoardRegion redRegion, List<Piece> blue, BoardRegion blueRegion) {
		this.initial = initial;
		redOptions = red;
		blueOptions = blue;
		redPlacementRegion = redRegion;
		bluePlacementRegion = blueRegion;
	}
	
	public BattleConfiguration(List<Piece> initial, List<Piece> red, List<Piece> blue) {
		this(initial, red, null, blue, null);
	}
	
	private List<Piece> redOptions;
	private List<Piece> blueOptions;
	private BoardRegion redPlacementRegion;
	private BoardRegion bluePlacementRegion;
	private List<Piece> initial;
}
