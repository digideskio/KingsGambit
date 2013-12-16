package kingsgambit.view.test;

import kingsgambit.controller.BattleController;
import kingsgambit.model.PlayerControlOption;
import kingsgambit.model.battle.AdvancedBattle;
import kingsgambit.model.battle.BattleParameters;
import kingsgambit.view.battle.BattleView;

public class BattleViewTest {
	public static void main(String... args) {
		BattleParameters parameters = new BattleParameters(new AdvancedBattle(), PlayerControlOption.HUMAN, PlayerControlOption.EASY);
		BattleController controller = new BattleController(parameters);

		BattleView battleView = new BattleView(controller, true, false);
		controller.setView(battleView);
		
		controller.getBattle().begin();
	}
}
