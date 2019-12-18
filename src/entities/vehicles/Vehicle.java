package entities.vehicles;

import entities.player.*;
import misc.MathUtils;
import world.Properties;
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
		this.addTag("vehicle");
		this.properties.set(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
	}
	
	public Player getDriver() {
		return driver;
	}
	
	public void setDriver(Player player) {
		this.driver = player;
	}
	
	private float frictionalEffect = 0.2f;
	
	public void update(float dt) {
		float speed = this.velocity.getLength();
		speed -= frictionalEffect * dt;
		speed = MathUtils.clip(0, maxSpeed, speed);
		this.velocity.setAngle(this.getRotation());
		this.velocity.setMagnitude(speed);
		//this.regenerateHitbox();
		//System.out.println(velocity);
	}
	
	private float acceleration = 1.0f;
	private float maxSpeed = 5.0f;
	
	public void accelerate(float dt) {
		float speed = this.velocity.getLength();
		speed += acceleration * dt;
		this.velocity.setMagnitude(speed);
	}
	
	private float brakePower = 3.0f;
	
	public void brake(float dt) {
		float speed = this.velocity.getLength();
		speed -= brakePower * dt;
		this.velocity.setMagnitude(speed);
	}
	
	private float turnSpeed = (float)Math.PI/2.0f;
	
	public void turnRight(float dt) {
		this.rotate(turnSpeed * dt);
		//this.velocity.r = turnSpeed;
	}
	
	public void turnLeft(float dt) {
		this.rotate(-turnSpeed * dt);
		//this.velocity.r = -turnSpeed;
	}
}
