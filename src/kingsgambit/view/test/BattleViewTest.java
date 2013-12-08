package kingsgambit.view.test;

import kingsgambit.controller.BattleController;
import kingsgambit.model.BattleParameters;
import kingsgambit.model.PlayerControlOption;
import kingsgambit.view.battle.BattleView;

public class BattleViewTest {
	public static void main(String... args) {
		BattleParameters parameters = new BattleParameters(PlayerControlOption.HUMAN, PlayerControlOption.EASY);
		BattleController controller = new BattleController(parameters);

		BattleView battleView = new BattleView(controller);
		controller.setView(battleView);
		
		controller.getBattle().begin();
	}
}
