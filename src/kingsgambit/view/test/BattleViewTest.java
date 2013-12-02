package kingsgambit.view.test;

import kingsgambit.controller.BattleController;
import kingsgambit.model.Battle;
import kingsgambit.model.BattleParameters;
import kingsgambit.model.BattleType;
import kingsgambit.model.Faction;
import kingsgambit.model.ai.SimpleAI;
import kingsgambit.view.battle.BattleView;

public class BattleViewTest {
	public static void main(String... args) {
		Battle battle = new Battle(new BattleParameters(BattleType.STANDARD));

		BattleController controller = new BattleController(battle);
		controller.setAI(Faction.BLUE, new SimpleAI(controller, battle.getBlue()));

		BattleView battleView = new BattleView(controller);
		controller.setView(battleView);
		
		battle.begin();
	}
}
