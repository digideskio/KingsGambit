package kingsgambit.model.battle;

import kingsgambit.model.PlayerControlOption;

public class BattleParameters {	
	public final PlayerControlOption redPlayerOption;
	public final PlayerControlOption bluePlayerOption;
	public final BattleType battleType;

	public BattleParameters(BattleType type, PlayerControlOption redOption, PlayerControlOption blueOption) {
		redPlayerOption = redOption;
		bluePlayerOption = blueOption;
		battleType = type;
	}
}
