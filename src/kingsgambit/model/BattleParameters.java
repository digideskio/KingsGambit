package kingsgambit.model;

public class BattleParameters {
	public final boolean hasPlacementPhase;

	public final BoardRegion redPlacement;
	public final BoardRegion bluePlacement;
	
	public final BattleType type;

	public BattleParameters(BattleType type, boolean place, BoardRegion redPlacement, BoardRegion bluePlacement) {
		this.type = type;
		hasPlacementPhase = place;
		this.redPlacement = redPlacement;
		this.bluePlacement = bluePlacement;
	}

	public BattleParameters(BattleType type) {
		this.type = type;
		hasPlacementPhase = false;
		redPlacement = null;
		bluePlacement = null;
	}
}
