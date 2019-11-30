package entities.vehicles;

import entities.Entity;

/**
 * Super class for vehicles such as cars, trucks, tractors, airplanes, bicycles, motorcycles
 */
public abstract class Vehicle extends Entity {

	private Entity driver;
	
	public Vehicle(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public abstract void accelerate();
	public abstract void brake();
	
}
