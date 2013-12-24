package kingsgambit.model.command;

public interface CommandExecutor {
	public void executeCommand(Command c);
	public void execute(MovePieceCommand c);
	public void execute(AttackPieceCommand c);
	public void execute(TurnPieceCommand c);
	public void execute(EndTurnCommand c);
	public void execute(PlacePieceCommand c);
	public void execute(UnplacePieceCommand c);
	public void execute(FactionReadyCommand c);
}
