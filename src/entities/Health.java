package entities;

import java.awt.Color;
import java.io.Serializable;

import entities.misc.TextParticle;
import misc.MathUtils;

public class Health implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private float health;
	private float maxHealth;
	private float displayHealth;
	
	private Entity owner;
	
	public Health(Entity owner, float health, float maxHealth) {
		this.owner = owner;
		this.health = health;
		this.maxHealth = maxHealth;
		this.displayHealth = 0;
	}
	
	public Health(Entity owner, float health) {
		this(owner, health,health);
	}
	
	private float timeSinceDamage = 999.0f;
	private float regenerationRate = 0.0f;
	private static final float minRegenWait = 3.0f;
	public void update(float dt) {
		timeSinceDamage+=dt;
		if (timeSinceDamage > minRegenWait) {
			this.heal(regenerationRate * dt);
		}
		//check for difference in health
		float dif = getPercent() - getDisplayPercent();
		float rate = dif * 15;
		if (Math.abs(rate) < 1)
			rate = MathUtils.sign(rate);
		this.displayHealth += rate * 3 * dt;
		this.displayHealth = MathUtils.clip(0, this.maxHealth, this.displayHealth);
	}
	
	/**
	 * Sets how quickly the health regenerates after being damaged
	 * in health units/second.
	 * @param rate
	 */
	public void setRegenerationRate(float rate) {
		this.regenerationRate = rate;
	}
	
	public boolean isDead() {
		return health <= 0;
	}
	
	public boolean hurt(float amount) {
		if (amount <= 0 || this.isDead()) //dont add insult to injury
			return false;
		this.setHealth(this.health - amount);
		this.timeSinceDamage = 0.0f; //reset this timer
		return true;
	}
	
	public void heal(float amount) {
		if (amount <= 0)
			return;
		if ((int)(this.health + amount)-(int)(this.health) > 0) {
			this.owner.popTextParticle("+1", Color.GREEN);
		}
		this.setHealth(this.health + amount);
	}
	
	public void setHealth(float health) {
		this.health = MathUtils.clip(0, maxHealth, health);
	}
	
	public float getValue() {
		return this.health;
	}
	
	public float getMaxValue() {
		return this.maxHealth;
	}
	
	public float getPercent() {
		return this.health/this.maxHealth;
	}
	
	public float getDisplayPercent() {
		return this.displayHealth/this.maxHealth;
	}

}
