package kingsgambit.model.event;

public class GameOverEvent implements GameEvent {

	public String toString() {
		return "GameOverEvent";
	}
	
	public void accept(GameEventHandler handler) {
		handler.handle(this);
	}

}
