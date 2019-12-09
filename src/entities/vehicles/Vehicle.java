package entities.vehicles;

import entities.player.*;
import entities.*;

/**
 * Super class for vehicles such as cars, trucks, tractors, airplanes, bicycles, motorcycles
 */
public abstract class Vehicle extends Entity {

	/**
	 * If driver is null then it should be operated by an AI, otherwise it uses input from that player
	 */
	private Player driver;
	
	public Vehicle(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public Player getDriver() {
		return driver;
	}
}
