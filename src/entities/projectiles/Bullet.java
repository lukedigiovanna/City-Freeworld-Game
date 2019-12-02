package entities.projectiles;

import java.awt.Color;

import entities.Entity;
import misc.Vector2;
import world.Camera;

public class Bullet extends Projectile {

	public Bullet(Entity owner, float x, float y, float angle) {
		super(owner, x, y, 0.2f, 0.1f, angle, 30);
	}
	
	public void draw(Camera c) {
		c.setColor(Color.BLACK);
		c.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
