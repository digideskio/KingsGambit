package kingsgambit.model.ai;

import java.util.List;

import kingsgambit.controller.BattleController;
import kingsgambit.model.Player;
import kingsgambit.model.command.Command;
import kingsgambit.model.command.EndTurnCommand;

public abstract class BaseAI implements AI {
	public void makeMoves() {
		List<Command> allCommands = player.getLegalCommands();
		if (allCommands.size() == 0) {
			System.out.println(player + " AI must pass");
			controller.executeCommandSynchronous(new EndTurnCommand());
		}
		
		controller.executeCommandSynchronous(bestMove());
		
		if (player.getMovesLeft() > 0)
			makeMoves();
	}
	
	protected abstract Command bestMove();
	
	public BaseAI(BattleController controller, Player player) {
		this.controller = controller;
		this.player = player;
	}
	
	protected Player player;
	protected BattleController controller;
}
