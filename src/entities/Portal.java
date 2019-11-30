package entities;

import java.awt.Color;
import java.util.List;

import world.Camera;
import world.Properties;
import world.Region;

public class Portal extends Entity {

	private Destination destination;
	
	public Portal(Destination destination, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.destination = destination;
		this.properties.set(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
	}

	private float r = 0, rv = 500;
	@Override
	public void draw(Camera camera) {
		camera.setColor(new Color((int)r,150,(int)r,(int)r));
		camera.fillRect(getX(),getY(),getWidth(),getHeight());
	}

	@Override
	public void update(float dt) {
		this.rotate((float)Math.PI*2*dt*0.5f);
		List<Entity> checkers = this.region.getEntities().get("player");
		for (Entity e : checkers)
			if (this.colliding(e))
				e.send(destination.region, destination.x, destination.y);
		r+=rv*dt;
		if (r > 255) {
			r = 255;
			rv*=-1;
		}
		if (r < 0) {
			r = 0;
			rv*=-1;
		}
		this.getRegion().addParticles(Particle.Type.GENERIC,Color.CYAN,3,0.2f,getX(),getY(),getWidth(),getHeight());
	}
	
	public static class Destination {
		public float x, y;
		public Region region;
		
		public Destination(Region reg, float x, float y) {
			this.x = x;
			this.y = y;
			this.region = reg;
		}
	}
}
