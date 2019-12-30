/**
 * 
 */
package entities;

import java.util.List;

import display.Animation;
import display.textures.*;
import world.Camera;
import world.Properties;

public class EntityObject extends Entity {
	
	private Texture texture;
	private Animation animation;
	
	public EntityObject(int id, float x, float y) {
		super(x,y);
		this.texture = TexturePack.current().getObjectTexture(id);
		this.animation = this.texture.getAnimation().copy(); 
		this.setDimension(this.texture.getDimension().copy());
		this.setHitboxToDimension();
		this.setProperty(Properties.KEY_INVULNERABLE, Properties.VALUE_INVULNERABLE_TRUE);
		this.addTag("entity_object");
	}

	@Override
	public void draw(Camera camera) {
		camera.drawImage(animation.getCurrentFrame(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	@Override
	public void update(float dt) {
		animation.animate(dt);
	}
}
