package kingsgambit.model;

import java.awt.Color;

public enum Faction {
	RED(Color.red), BLUE(Color.blue);
	
	public final Color color;
	Faction(Color c) {
		color = c;
	}
}
