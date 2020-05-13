package entities.misc;

import java.io.Serializable;
import java.util.List;

import entities.Entity;
import world.Camera;
import world.Properties;

public class Portal extends Entity {

	private Destination destination;
	
	public Portal(Destination destination, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.destination = destination;
		setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
		setProperty(Properties.KEY_HAS_COLLISION, Properties.VALUE_HAS_COLLISION_FALSE);
	}

	@Override
	public void draw(Camera camera) {
		//nothing to draw
	}

	@Override
	public void update(float dt) {
		this.rotate((float)Math.PI*2*dt*0.5f);
		List<Entity> checkers = this.getRegion().getEntities().get("player");
		for (Entity e : checkers)
			if (this.colliding(e)) 
				e.send(destination.regionNumber, destination.x, destination.y);
	}
	
	public static class Destination implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public float x, y;
		public int regionNumber;
		
		public Destination(int reg, float x, float y) {
			this.x = x;
			this.y = y;
			this.regionNumber = reg;
		}
	}
}
