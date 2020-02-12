package entities.projectiles;

import java.awt.Color;

import entities.Entity;
import world.Camera;
import world.event.CollisionEvent;

public class Grenade extends Projectile {

	public Grenade(Entity owner, float x, float y, float angle) {
		super(owner, x, y, 0.15f, 0.15f, angle, 3); 
		this.setDamage(15f);

		this.addCollisionEvent(CollisionEvent.SLIDE);
		this.removeCollisionEvent(CollisionEvent.STOP);
	}

	@Override
	public void draw(Camera camera) {
		camera.setColor(Color.GREEN);
		camera.fillOval(getX(), getY(), getWidth(), getHeight());
	}
}
