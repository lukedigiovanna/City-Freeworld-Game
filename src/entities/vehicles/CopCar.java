package entities.vehicles;

import entities.player.Player;

public class CopCar extends Car {
	
	private Player target;
	
	public CopCar(float x, float y) {
		super(Car.Model.COP_CAR,x,y);
		
		this.addTag("cop_car");
	}
	
	
}
