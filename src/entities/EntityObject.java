/**
 * 
 */
package entities;

import java.awt.image.BufferedImage;
import java.util.List;

import world.Camera;

public class EntityObject extends Entity {

	private BufferedImage image;
	
	public EntityObject(BufferedImage image, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.image = image;
		this.addTag("object");
	}

	@Override
	public void draw(Camera camera) {
		camera.drawImage(image, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	@Override
	public void update(float dt) {
		if (true)
			return;
		
		List<Entity> killers = this.getRegion().getEntities().get("player");
		
		for (Entity e : killers)
			if (e.colliding(this))
				this.destroy();
	}

}
