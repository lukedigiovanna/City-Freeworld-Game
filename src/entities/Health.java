package entities;

import misc.MathUtils;

public class Health {
	
	private float health;
	private float maxHealth;
	
	public Health(float health, float maxHealth) {
		this.health = health;
		this.maxHealth = maxHealth;
	}
	
	public boolean isDead() {
		return health <= 0;
	}
	
	public void hurt(float amount) {
		this.health -= amount;
	}
	
	public void heal(float amount) {
		this.health += amount;
	}
	
	public void setHealth(float health) {
		this.health = MathUtils.clip(0, maxHealth, health);
	}
	
	public float getValue() {
		return this.health;
	}
	
	public float getPercent() {
		return this.health/this.maxHealth;
	}

}
