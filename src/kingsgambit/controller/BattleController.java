package kingsgambit.controller;

import java.util.HashMap;

import jmotion.animation.Animation;
import kingsgambit.model.Battle;
import kingsgambit.model.Faction;
import kingsgambit.model.ai.AI;
import kingsgambit.model.command.Command;
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
		}
	}
	
	public Battle getBattle() {
		return battle;
	}
	
	public void setAI(Faction f, AI ai) {
		factionAIs.put(f, ai);
	}

	public void setView(BattleView view) {
		this.view = view;
	}
	
	public BattleController(Battle battle) {
		this.battle = battle;
		battle.setController(this);
		
		factionAIs = new HashMap<Faction, AI>();
	}
	
	private Battle battle;
	private BattleView view;
	private HashMap<Faction, AI> factionAIs;
}
