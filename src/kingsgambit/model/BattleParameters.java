package kingsgambit.model;

public class BattleParameters {	
	public final PlayerControlOption redPlayerOption;
	public final PlayerControlOption bluePlayerOption;

	public BattleParameters(PlayerControlOption redOption, PlayerControlOption blueOption) {
		redPlayerOption = redOption;
		bluePlayerOption = blueOption;
	}
}
