package entities.projectiles;

import java.awt.Color;

import entities.Entity;
import misc.MathUtils;
import world.Camera;
import world.event.CollisionEvent;

public class Shard extends Projectile {
	public Shard(Entity owner, float x, float y, float angle) {
		super(owner,x,y,MathUtils.random(0.025f, 0.075f),MathUtils.random(0.05f, 0.1f),angle,MathUtils.random(3, 7f));
		
		this.setDamage(MathUtils.random(4, 10));
		this.setHasFriendlyFire(true);
		this.getVelocity().r = MathUtils.random(4f,10f);
	}

	@Override
	public void draw(Camera camera) {
		camera.setColor(Color.DARK_GRAY);
		camera.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
