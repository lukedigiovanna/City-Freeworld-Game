package entities.projectiles;

import java.awt.Color;

import entities.Entity;
import entities.misc.Particle;
import misc.MathUtils;
import world.Camera;
import world.event.CollisionEvent;

public class Grenade extends Projectile {

	public Grenade(Entity owner, float x, float y, float angle) {
		super(owner, x, y, 0.15f, 0.15f, angle, 3); 
		
		this.dontDestroyOnHit();
		
		this.removeCollisionEvent(CollisionEvent.STOP);
	}
	
	public void destroy() {
		for (int i = 0; i < 30; i++) {
			this.getRegion().add(new Shard(this.getOwner(),this.centerX(),this.centerY(),MathUtils.random((float)Math.PI*2)));
		}
		super.destroy();
	}
	
	private static final float ACCELERATION = -1f;
	
	public void update(float dt) {
		super.update(dt);
		float speed = this.getVelocity().getLength();
		speed += ACCELERATION * dt;
		if (speed < 0)
			speed = 0;
		this.getVelocity().setMagnitude(speed);
	}

	@Override
	public void draw(Camera camera) {
		camera.setColor(Color.GREEN);
		camera.fillOval(getX(), getY(), getWidth(), getHeight());
	}
}
