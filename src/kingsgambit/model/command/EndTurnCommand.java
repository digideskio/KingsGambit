package kingsgambit.model.command;

import kingsgambit.model.Square;

public class EndTurnCommand implements Command {

	public void accept(CommandExecutor cex) {
		cex.execute(this);
	}

	public Square getOperativeSquare() {
		return null;
	}
	
	public String toString() {
		return "End the Turn";
	}

	public int getCost() {
		return 0;
	}

	public EndTurnCommand() {
	}
}
