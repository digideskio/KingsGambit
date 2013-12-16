package kingsgambit.model.event;


public class BattleBeginEvent implements GameEvent {

	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}	
}
