package kingsgambit.model.event;

public interface GameEventHandler {
	public void handle(PieceMoveEvent event);
	public void handle(PieceTurnEvent event);
	public void handle(AttackEvent event);
	public void handle(BeginTurnEvent event);
	public void handle(DieEvent event);
	public void handle(RetreatEvent event);
	public void handle(PiecePlaceEvent event);
	public void handle(BattleBeginEvent event);
}
