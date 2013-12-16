package kingsgambit.model.command;

import kingsgambit.model.Faction;
import kingsgambit.model.Square;

public class FactionReadyCommand implements Command {

	public void accept(CommandExecutor cex) {
		cex.execute(this);
	}

	public Square getOperativeSquare() {
		return null;
	}

	public int getCost() {
		return 0;
	}
	
	public String toString() {
		return "FactionReadyCommand(" + faction + ")";
	}

	public Faction getFaction() {
		return faction;
	}
	
	public FactionReadyCommand(Faction faction) {
		this.faction = faction;
	}
	
	private Faction faction;
}
