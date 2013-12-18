package kingsgambit.model.ai;

import java.util.List;

import kingsgambit.controller.BattleController;
import kingsgambit.model.Player;
import kingsgambit.model.battle.BattleConfiguration;
import kingsgambit.model.command.Command;
import kingsgambit.model.command.EndTurnCommand;
import kingsgambit.model.command.FactionReadyCommand;

public abstract class BaseAI implements AI {
	public void makeMoves() {
		if (controller.getBattle().isGameOver())
			return;

		List<Command> allCommands = player.getLegalCommands();
		if (allCommands.size() == 0) {
			System.out.println(player + " AI must pass");
			controller.executeCommandSynchronous(new EndTurnCommand());
		}
		
		controller.executeCommandSynchronous(bestMove());
		
		if (player.getMovesLeft() > 0)
			makeMoves();
	}
	
	public void battleStart() {
		BattleConfiguration config = controller.getBattle().getConfiguration();
		if (config.requiresPlacementPhase())
			placePieces(config);
		
		controller.executeCommandSynchronous(new FactionReadyCommand(player.faction));
	}
	
	protected abstract Command bestMove();
	
	protected abstract void placePieces(BattleConfiguration configuration);
	
	public BaseAI(BattleController controller, Player player) {
		this.controller = controller;
		this.player = player;
	}
	
	protected Player player;
	protected BattleController controller;
}
