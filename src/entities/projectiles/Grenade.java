package entities.projectiles;

import java.awt.Color;

import entities.Entity;
import misc.Vector2;
import world.Camera;

public class Grenade extends Projectile {

	public Grenade(Entity owner, float x, float y, float angle) {
		super(owner, x, y, 0.35f, 0.35f, angle, 3); 
	}

	@Override
	public void draw(Camera camera) {
		camera.setColor(Color.GREEN);
		camera.fillOval(getX(), getY(), getWidth(), getHeight());
	}

}
