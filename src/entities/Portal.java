package entities;

import java.awt.Color;

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
		
	}
	
	public class Destination {
		public float x, y;
		public Region destinationRegion;
		
		public Destination(Region reg, float x, float y) {
			this.x = x;
			this.y = y;
			this.destinationRegion = reg;
		}
	}
}
