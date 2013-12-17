package kingsgambit.controller;

import java.util.HashMap;

import jmotion.animation.Animation;
import kingsgambit.model.Faction;
import kingsgambit.model.PlayerControlOption;
import kingsgambit.model.ai.AI;
import kingsgambit.model.ai.SimpleAI;
import kingsgambit.model.battle.Battle;
import kingsgambit.model.battle.BattleParameters;
import kingsgambit.model.command.Command;
import kingsgambit.model.event.BattleBeginEvent;
import kingsgambit.model.event.BeginTurnEvent;
import kingsgambit.model.event.GameEvent;
import kingsgambit.view.battle.BattleView;

public class BattleController {
	
	public void executeCommand(final Command command) {
		System.out.println("Controller executing " + command);
		new Thread(new Runnable() {
			public void run() {
				// Pass the command to the model
				battle.executeCommand(command);
			}
		}).start();
		System.out.println(" ..... animation thread moving on");
	}
	
	public void executeCommandSynchronous(final Command command) {
		System.out.println("Controller executing " + command);
		// Pass the command to the model
		battle.executeCommand(command);
	}
	
	public void handle(GameEvent event) {
		System.out.println("Handling " + event);
		
		// Tell the UI about the event
		Animation eventAnimation = view.animate(event);
		
		if (eventAnimation != null)
			eventAnimation.finish();
		System.out.println("....animation finished");
		
		// Tell the AI if it can make moves
		if (event instanceof BeginTurnEvent) {
			Faction whosTurn = ((BeginTurnEvent)event).faction;
			if (factionAIs.containsKey(whosTurn))
				factionAIs.get(whosTurn).makeMoves();
		} else if (event instanceof BattleBeginEvent) {
			for (AI ai : factionAIs.values())
				ai.battleStart();
		}
	}
	
	public Battle getBattle() {
		return battle;
	}
	
	public void setView(BattleView view) {
		this.view = view;
	}
	
	public BattleController(BattleParameters parameters) {
		battle = new Battle(parameters);
		battle.setController(this);
		
		factionAIs = new HashMap<Faction, AI>();

		if (parameters.redPlayerOption != PlayerControlOption.HUMAN)
			addAI(Faction.RED, parameters.redPlayerOption);
		if (parameters.bluePlayerOption != PlayerControlOption.HUMAN)
			addAI(Faction.BLUE, parameters.bluePlayerOption);
	}

	private void addAI(Faction f, PlayerControlOption option) {
		// TODO generate the AI at an appropriate difficulty level
		factionAIs.put(f, new SimpleAI(this, battle.getPlayer(f)));
	}

	private Battle battle;
	private BattleView view;
	private HashMap<Faction, AI> factionAIs;
}
