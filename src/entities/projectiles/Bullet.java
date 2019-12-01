package entities.projectiles;

import java.awt.Color;

import entities.Entity;
import misc.Vector2;
import world.Camera;

public class Bullet extends Projectile {

	public Bullet(Entity owner, float x, float y, Vector2 velocity) {
		super(owner, x, y, 0.2f, 0.1f);
		this.velocity = velocity;
		this.position.r = this.velocity.getAngle();
	}
	
	public void draw(Camera c) {
		c.setColor(Color.BLACK);
		c.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
