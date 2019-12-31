package entities.projectiles;

import java.awt.Color;
import java.awt.image.BufferedImage;

import entities.Entity;
import misc.ImageTools;
import misc.Vector2;
import world.Camera;

public class Bullet extends Projectile {

	public Bullet(Entity owner, float x, float y, float angle) {
		super(owner, x, y, 0.1f, 0.05f, angle, 20);
	}
	
	private BufferedImage bullet = ImageTools.getImage("assets/images/weapons/bullet.png");
	
	public void draw(Camera c) {
		c.drawImage(bullet, getX(), getY(), getWidth(), getHeight());
	}
}
