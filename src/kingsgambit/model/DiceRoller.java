package kingsgambit.model;

import java.util.Random;

public class DiceRoller {
	public WeaponDieFace[] roll(int numDice) {
		WeaponDieFace[] result = new WeaponDieFace[numDice];
		
		for (int i = 0; i<numDice; ++i)
			result[i] = faces[randy.nextInt(faces.length)];
	
		return result;
	}

	public DiceRoller(WeaponDieFace... faces) {
		randy = new Random();
		this.faces = faces;
	}
	
	private WeaponDieFace[] faces;
	private Random randy;
}
