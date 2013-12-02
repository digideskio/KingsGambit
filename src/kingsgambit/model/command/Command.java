package kingsgambit.model.command;

import kingsgambit.model.Square;

public interface Command {
	public void accept(CommandExecutor cex);
	public Square getOperativeSquare();
	public int getCost();
}
