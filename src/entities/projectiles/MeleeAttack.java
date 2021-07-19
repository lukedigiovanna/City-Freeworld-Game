package entities.projectiles;

import java.awt.Color;

import entities.Entity;
import world.Camera;

public class MeleeAttack extends Projectile {
	private static final float SPEED = 10;
	
	private final float lifetime;
	
	public MeleeAttack(Entity owner, float x, float y, float angle, float range) {
		super(owner,x,y,0.25f,0.25f,angle,SPEED);
		this.lifetime = range / SPEED;
	}
	

	@Override
	public void draw(Camera camera) {
		camera.setColor(new Color(255, 0, 0, 125));
		camera.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	public void update(float dt) {
		super.update(dt);
		if (this.getAge() >= this.lifetime) {
			this.destroy();
		}
	}
}
