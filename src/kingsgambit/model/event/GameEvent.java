package kingsgambit.model.event;

public interface GameEvent {
	public void accept(GameEventHandler handler);
}
