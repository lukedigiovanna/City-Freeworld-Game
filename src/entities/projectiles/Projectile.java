package entities.projectiles;

import java.util.List;

import entities.Entity;
import world.Camera;
import world.Properties;

/*
 * Super class for objects that are shot like bullets, grenades, etc.
 */
public abstract class Projectile extends Entity {

	private Entity owner; //the entity that is responsible for releasing the bullet
	
	protected boolean destroyOnHit = true;
	private float damage = 0;
	private float lifeSpan = 5.0f; //5 seconds by default; can dictate like when a grenade goes off
	
	public Projectile(Entity owner, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.owner = owner;
	}
	
	public void dontDestroyOnHit() {
		destroyOnHit = false;
	}
	
	/**
	 * Sets the amount of damage that the projectile deals on 
	 * @param damage
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}

	@Override
	public void update(float dt) {
		if (this.age >= this.lifeSpan) {
			this.destroy();
			return; //were dead so no more checking.
		}
		
		//if we dont destroy on hit, then its not worth checking for collision
		if (!this.destroyOnHit) 
			return; 
		
		//look through the entities and see if its colliding with them
		//if it destroys on hit then deal the damage and destroy.
		List<Entity> others = this.region.getEntities().get();
		for (int i = 0; i < others.size(); i++) {
			if (i > others.size()-1)
				break; //avoid concurrent modification
			Entity e = others.get(i);
			if (e == owner)
				return; //dont hurt the entity that shot the projectile
			//check if the entity is kill able
			if (e.getProperty(Properties.KEY_INVULNERABLE) == Properties.VALUE_INVULNERABLE_FALSE) {
				if (this.colliding(e)) {
					e.hurt(damage);
					for (String s : e.getTags())
						System.out.println(s);
					this.destroy();
					return; //already hit one.. were done now
				}
			}
		}
	}
}
