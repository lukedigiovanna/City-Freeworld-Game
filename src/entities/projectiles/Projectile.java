package entities.projectiles;

import java.util.List;

import entities.Entity;
import misc.Vector2;
import world.Properties;

/*
 * Super class for objects that are shot like bullets, grenades, etc.
 */
public abstract class Projectile extends Entity {

	private Entity owner; //the entity that is responsible for releasing the bullet
	
	protected boolean destroyOnHit = true;
	private boolean hasFriendlyFire = false;
	private float damage = 1;
	private float lifeSpan = 5.0f; //5 seconds by default; can dictate like when a grenade goes off
	
	public Projectile(Entity owner, float x, float y, float width, float height, Vector2 vi) {
		super(x, y, width, height);
		this.owner = owner;
		this.setVelocity(vi);
		this.setRotation(vi.getAngle());
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
		this.setProperty(Properties.KEY_DESTROY_ON_COLLISION, Properties.VALUE_DESTROY_ON_COLLISION_TRUE);
		
		this.setVerticalHeight(7.5f);
	}  
	
	public Projectile(Entity owner, float x, float y, float width, float height, float angle, float speed) {
		this(owner,x,y,width,height,new Vector2(speed*(float)Math.cos(angle),speed*(float)Math.sin(angle)));
	}
	
	public void setHasFriendlyFire(boolean value) {
		this.hasFriendlyFire = value;
	}
	
	public Entity getOwner() {
		return this.owner;
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
		if (this.getAge() >= this.lifeSpan) {
			this.destroy();
			return; //were dead so no more checking.
		}
		
		//if we dont destroy on hit, then its not worth checking for collision
		if (!this.destroyOnHit) 
			return; 
		
		//look through the entities and see if its colliding with them
		//if it destroys on hit then deal the damage and destroy.
		List<Entity> others = this.getRegion().getEntities().get();
		for (int i = 0; i < others.size(); i++) {
			if (i > others.size()-1)
				break; //avoid out of bounds (concurrency)
			Entity e = others.get(i);
			if ((e == owner || e == this) && !this.hasFriendlyFire)
				continue; //dont hurt the entity that shot the projectile
			//check if the entity is kill able
			if (e.getProperty(Properties.KEY_INVULNERABLE) == Properties.VALUE_INVULNERABLE_FALSE && !e.isDestroyed()) {
				if (this.colliding(e)) {
					e.hurt(damage,this.owner);
					this.destroy();
					return; //already hit one.. were done now
				}
			}
		}
	}
}
