package entities.projectiles;

import java.awt.Color;

import entities.Entity;
import world.Camera;

public class ShotgunPellet extends Projectile {
	public ShotgunPellet(Entity owner, float x, float y, float angle) {
		super(owner,x,y,0.025f,0.025f,angle,20);
	}
	
	public void draw(Camera c) {
		c.setColor(Color.GRAY);
		c.fillOval(getX(), getY(), getWidth(), getHeight());
	}
}
