package kingsgambit.model.event;

import kingsgambit.model.Faction;

public class BeginTurnEvent implements GameEvent {

	public final Faction faction; 
	
	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}

	public String toString() {
		return "BeginTurnEvent(" + faction + ")";
	}
	
	
	public BeginTurnEvent(Faction f) {
		faction = f;
	}
}
