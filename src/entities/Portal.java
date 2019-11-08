package entities;

import java.awt.Color;
import java.util.List;

import world.Camera;
import world.Region;

public class Portal extends Entity {

	private Destination destination;
	
	public Portal(Destination destination, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.destination = destination;
	}

	@Override
	public void draw(Camera camera) {
		camera.setColor(Color.MAGENTA);
		camera.fillRect(getX(),getY(),getWidth(),getHeight());
	}

	@Override
	public void update(float dt) {
		this.rotate((float)Math.PI*2*dt*1.5f);
		List<Entity> checkers = this.region.getEntities().get("player");
		for (Entity e : checkers)
			if (this.colliding(e))
				e.send(destination.region, destination.x, destination.y);
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
