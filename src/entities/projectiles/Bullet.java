package entities.projectiles;

import java.awt.Color;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.misc.Particle;
import misc.ImageTools;
import world.Camera;

import world.WorldObject;
import world.event.CollisionEvent;
import misc.Line;

public class Bullet extends Projectile {

	public Bullet(Entity owner, float x, float y, float angle) {
		super(owner, x, y, 0.1f, 0.05f, angle, 20);
		
		// this.addCollisionEvent((WorldObject obj, Line wall) -> {
		// 	obj.getRegion().addParticles(Particle.Type.SPARKLES, Color.ORANGE, 4, 0.15f, obj.centerX()-0.2f, obj.centerY()-0.2f, 0.4f, 0.4f);
		// 	this.destroy();
		// });

		this.addCollisionEvent(CollisionEvent.SLIDE);

		this.removeCollisionEvent(CollisionEvent.STOP);
	}
	
	private transient BufferedImage bullet = ImageTools.getImage("assets/images/weapons/bullet.png");
	
	public void draw(Camera c) {
		c.drawImage(bullet, getX(), getY(), getWidth(), getHeight());
	}
}
