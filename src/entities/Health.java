package entities;

import java.io.Serializable;

import misc.MathUtils;

public class Health implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private float health;
	private float maxHealth;
	private float displayHealth;
	
	public Health(float health, float maxHealth) {
		this.health = health;
		this.maxHealth = maxHealth;
		this.displayHealth = 0;
	}
	
	public Health(float health) {
		this(health,health);
	}
	
	public void update(float dt) {
		//check for difference in health
		float dif = getPercent() - getDisplayPercent();
		float rate = dif * this.health;
		this.displayHealth += rate * dt;
		this.displayHealth = getPercent() - getDisplayPercent() < 0.05 ? this.health : this.displayHealth;
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
	
	public float getDisplayPercent() {
		return this.displayHealth/this.maxHealth;
	}

}
