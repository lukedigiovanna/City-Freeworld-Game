/**
 * 
 */
package entities;

import java.awt.image.BufferedImage;
import java.util.List;

import display.Animation;
import world.Camera;

public class EntityObject extends Entity {
	
	private BufferedImage image;
	private Animation animation;
	
	public EntityObject(BufferedImage image, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.image = image;
		this.addTag("object");
	}
	
	public EntityObject(Animation animation, float x, float y, float width, float height) {
		super(x,y,width,height);
		this.animation = animation;
		this.animation.randomize();
		this.addTag("object");
	}

	@Override
	public void draw(Camera camera) {
		BufferedImage img = image;
		if (img == null)
			img = animation.getCurrentFrame();
		camera.drawImage(img, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	@Override
	public void update(float dt) {
		if (animation != null)
			animation.animate(dt);
		
		List<Entity> killers = this.getRegion().getEntities().get("player");
		
		for (Entity e : killers)
			if (e.colliding(this))
				this.destroy();
	}

}
