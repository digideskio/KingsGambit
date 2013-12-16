package kingsgambit.model.battle;


public abstract class BattleType {
	public static BattleType STANDARD = new StandardBattle();
	public static BattleType[] TYPES = {STANDARD, new AdvancedBattle()};

	public String getName() { 
		return name;
	}
	
	public String toString() {
		return name;
	}

	public String getDescription() { 
		return description;
	}
	
	public abstract BattleConfiguration createConfiguration();
	
	public BattleType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	private String name;
	private String description;
}
