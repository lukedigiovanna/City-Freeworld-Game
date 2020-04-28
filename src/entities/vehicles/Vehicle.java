package entities.vehicles;

import misc.MathUtils;
import world.Properties;
import entities.*;

/**
 * Super class for vehicles such as cars, trucks, tractors, airplanes, bicycles, motorcycles
 */
public abstract class Vehicle extends Entity {

	/**
	 * If driver is null then it should be operated by an AI, otherwise it uses input from that Human
	 */
	private Human driver;
	
	public Vehicle(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.addTag("vehicle");
		
		this.setFieldOfView((float)Math.PI/8f);
		
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
		this.setVerticalHeight(7.5f);
		//this.setProperty(Properties.KEY_HAS_RIGID_BODY, Properties.VALUE_HAS_RIGID_BODY_TRUE);
		this.setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
	}
	
	public Human getDriver() {
		return driver;
	}
	
	public void setDriver(Human Human) {
		this.driver = Human;
	}
	
	private float frictionalEffect = 0.2f;
	
	public void update(float dt) {
		this.timeSinceLastBrake+=dt;
		float speed = this.getVelocity().getLength();
		speed -= frictionalEffect * dt;
		speed = MathUtils.clip(0, maxSpeed, speed);
		this.getVelocity().setMagnitude(speed);
		this.getVelocity().setAngle(this.getRotation());
	}
	
	private float acceleration = 1.0f;
	private float maxSpeed = 5.0f;
	
	public void accelerate(float dt) {
		if (this.timeSinceLastBrake < 1f) //min wait of 1 second before the car can start accelerating again.
			return;
		float speed = this.getVelocity().getLength();
		speed += acceleration * dt;
		this.getVelocity().setMagnitude(speed);
	}
	
	private float brakePower = 2.0f;
	private float timeSinceLastBrake = MathUtils.INFINITY;
	public void brake(float dt) {
		float speed = this.getVelocity().getLength();
		speed -= brakePower * dt;
		if (speed < 0) speed = 0;
		this.getVelocity().setMagnitude(speed);
		this.timeSinceLastBrake = 0;
		//this.getRegion().addParticles(Particle.Type.TIRE_MARK, java.awt.Color.BLACK, 1, 0.0f, centerX(), centerY(), 0.1f, 0.1f);
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
