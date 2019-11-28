package entities.vehicles;

import entities.Entity;

public abstract class Vehicle extends Entity {

	private boolean aiControlled = true;
	
	public Vehicle(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	
}
